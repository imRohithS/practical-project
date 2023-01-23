package com.flink.usecase.main;

import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.io.TextInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import com.flink.usecase.function.AccountObject;
import com.flink.usecase.function.CustomerAccntObject;
import com.flink.usecase.function.CustomerAccntRoleObject;
import com.flink.usecase.function.CustomerObject;
import com.flink.usecase.function.CustomerTxnObject;
import com.flink.usecase.model.Account;
import com.flink.usecase.model.Customer;
import com.flink.usecase.model.CustomerAccnt;
import com.flink.usecase.model.CustomerAccntRole;
import com.flink.usecase.model.CustomerTxn;
import com.flink.usecase.model.Query;
import com.flink.usecase.model.TxnDetails;
import com.flink.usecase.process.TransactionQuery;
import com.flink.usecase.process.function.AccntTxn;
import com.flink.usecase.process.function.CustAcctRoleMap;
import com.flink.usecase.process.function.CustomerAcctMapping;
import com.flink.usecase.process.function.CustomerMapping;

public class FlinkIntegration {

	public static final MapStateDescriptor<String, Query> txnQueryDescriptor = new MapStateDescriptor<String, Query>(
			"txnQuery", BasicTypeInfo.STRING_TYPE_INFO, TypeInformation.of(new TypeHint<Query>() {
			}));
	public static final MapStateDescriptor<String, CustomerTxn> txnAccDescriptor = new MapStateDescriptor<String, CustomerTxn>(
			"CustomerTxn", BasicTypeInfo.STRING_TYPE_INFO, TypeInformation.of(new TypeHint<CustomerTxn>() {
			}));
	public static final MapStateDescriptor<String, CustomerAccntRole> custRoleDescriptor = new MapStateDescriptor<String, CustomerAccntRole>(
			"CustomerAccntRole", BasicTypeInfo.STRING_TYPE_INFO, TypeInformation.of(new TypeHint<CustomerAccntRole>() {
			}));
	public static final MapStateDescriptor<String, TxnDetails> custAcctRoleDescriptor = new MapStateDescriptor<String, TxnDetails>(
			"TxnDetails", BasicTypeInfo.STRING_TYPE_INFO, TypeInformation.of(new TypeHint<TxnDetails>() {
			}));

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {

		String custJsonLocation = "C:/Users/0039ER744/Documents/Apache Flink/Task/POC/CUST_JSON_3.json";
		String acctJsonLocation = "C:/Users/0039ER744/Documents/Apache Flink/Task/POC/ACCT_JSON_3.json";
		String custTxnJsonLocation = "C:/Users/0039ER744/Documents/Apache Flink/Task/POC/CASH_TRXN_JSON_3.json";
		String custAcctJsonLocation = "C:/Users/0039ER744/Documents/Apache Flink/Task/POC/CUST_ACCT_JSON_3.json";
		String custAcctTypeJsonLocation = "C:/Users/0039ER744/Documents/Apache Flink/Task/POC/CUST_ACCT_ROLE_JSON_3.json";
		String queryJson = "C:/Users/0039ER744/Documents/Apache Flink/Task/POC/QUERY_JSON.json";

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		FlinkKafkaConsumer<String> flinkKafkaConsumer = createStringConsumerForTopic();

		DataStream<String> customer = env.readFile(new TextInputFormat(new Path(custJsonLocation)), custJsonLocation);

		DataStream<Customer> custStream = customer.flatMap(new CustomerObject()).keyBy(a -> a.getCustIntrlId());

		DataStream<String> acct = env.readFile(new TextInputFormat(new Path(acctJsonLocation)), acctJsonLocation);

		DataStream<Account> acctStream = acct.flatMap(new AccountObject()).keyBy(b -> b.getPrmryCustIntrlId());

		DataStream<String> custAcct = env.readFile(new TextInputFormat(new Path(custAcctJsonLocation)),
				custAcctJsonLocation);

		DataStream<CustomerAccnt> custAcctStream = custAcct.flatMap(new CustomerAccntObject());

		DataStream<String> custAcctRole = env.readFile(new TextInputFormat(new Path(custAcctTypeJsonLocation)),
				custAcctTypeJsonLocation);

		DataStream<CustomerAccntRole> custAcctRoleStream = custAcctRole.flatMap(new CustomerAccntRoleObject());

		DataStream<String> cashTxn = env.addSource(flinkKafkaConsumer);
//		DataStream<String> cashTxn = env.readFile(new TextInputFormat(new Path(custTxnJsonLocation)),
//				custTxnJsonLocation);

		DataStream<CustomerTxn> cashTxnStream = cashTxn.flatMap(new CustomerTxnObject()).keyBy(b -> b.getAcctIntrlId());

		DataStream<TxnDetails> custAcctMap = custStream.connect(acctStream).process(new CustomerAcctMapping())
				.filter(a -> a.getCust() != null && a.getAcc() != null).keyBy(a -> a.getAcc().getAcctIntrlId());

		DataStream<TxnDetails> custAcctTxnMap = custAcctMap.connect(cashTxnStream).process(new AccntTxn())
				.filter(a -> a.getAcc() != null && a.getCust() != null && a.getCustTxn() != null);

//		BroadcastStream<CustomerAccntRole> custRoleBroadcast = custAcctRoleStream.broadcast(custRoleDescriptor);
		DataStream<TxnDetails> custAcctRoleMap = custAcctStream.connect(custAcctRoleStream)
				.keyBy(a -> a.getCustAcctRoleCd(), b -> b.getCustAcctRoleCd()).process(new CustAcctRoleMap());

		BroadcastStream<TxnDetails> custRoleTypeBroadcast = custAcctRoleMap.broadcast(custAcctRoleDescriptor);

		DataStream<TxnDetails> customerTxnDetails = custAcctTxnMap.keyBy(a -> a.getCust().getCustIntrlId())
				.connect(custRoleTypeBroadcast).process(new CustomerMapping());
		DataStream<String> query = env.readFile(new TextInputFormat(new Path(queryJson)), queryJson);

		DataStream<Query> queryStream = query.flatMap(new QueryMapper());

		BroadcastStream<Query> queryBroadcast = queryStream.broadcast(txnQueryDescriptor);

		DataStream<String> evaluate = customerTxnDetails.keyBy(a -> a.getAcc().getAcctIntrlId()).connect(queryBroadcast)
				.process(new TransactionQuery());
		evaluate.print();

//		DataStream<TxnDetails> transactionDetails=customerTxnDetails.connect(queryStream).process(new TxnQueryMap());
//		transactionDetails.print();
//		BroadcastStream<CustomerTxn> custTxnBroadCast = cashTxnStream.broadcast(txnAccDescriptor);
//		custAcctMap.keyBy(a -> a.getAcc().getAcctIntrlId()).connect(custTxnBroadCast).process(new CustAcctTxnMap()).print();
//		cashTxnStream.connect(custAcctMap)
//		.keyBy(b -> b.getAcctIntrlId() , a -> a.getAcc().getAcctIntrlId()).process(new AccntTxnSwap()).print();
//		DataStream<String> calculateTXN = custAcctTxnMap.keyBy(a -> a.getAcc().getAcctIntrlId())
//				.window(SlidingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(1))).process(new CalculateTXN());
//		DataStream<String> evaluate = custAcctTxnMap.keyBy(a -> a.getAcc().getAcctIntrlId()).connect(queryStream)
//				.process(new CalculateTXNQuery());
//		DataStream<String> evaluate = custAcctTxnMap.connect(queryStream).process(new CalculateTXNQuery());
//		custAcctTxnMap.print();
//		custAcctMap.print();
//		calculateTXN.print();
//		HR.print();
//		MR.print();

		env.execute();

	}

	@SuppressWarnings("deprecation")
	private static FlinkKafkaConsumer<String> createStringConsumerForTopic() {
		String topic = "sample_1";
		String kafkaAddress = "localhost:9092";
		String kafkaGroup = "group-id";

		Properties props = new Properties();
		props.setProperty("bootstrap.servers", kafkaAddress);
		props.setProperty("group.id", kafkaGroup);
		FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), props);

		return consumer;
	}

}
