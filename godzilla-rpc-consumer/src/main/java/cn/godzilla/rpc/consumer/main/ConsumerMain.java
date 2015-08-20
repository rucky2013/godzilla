package cn.godzilla.rpc.consumer.main;

import java.util.ArrayList;
import java.util.Random;

import cn.godzilla.model.RpcResult;
import cn.godzilla.rpc.api.RpcFactory;
import cn.godzilla.rpc.benchmark.dataobject.FullAddress;
import cn.godzilla.rpc.benchmark.dataobject.Person;
import cn.godzilla.rpc.benchmark.dataobject.PersonInfo;
import cn.godzilla.rpc.benchmark.dataobject.PersonStatus;
import cn.godzilla.rpc.benchmark.dataobject.Phone;
import cn.godzilla.rpc.benchmark.service.HelloService;
import cn.godzilla.rpc.main.Util;
import cn.godzilla.service.PropConfigProviderService;
import cn.godzilla.service.PropConfigService;

public class ConsumerMain {
	/*private static volatile int size = 3;

	public static void main(String args[]) throws Exception {
		final RpcFactory rpcFactory = Util.getRpcFactoryImpl();
		String serverIp = "127.0.0.1";

		final HelloService reference1 = rpcFactory.getReference(HelloService.class, serverIp);
		String ret = reference1.helloWorld("111");
		
		final PropConfigProviderService reference2 = rpcFactory.getReference(PropConfigProviderService.class, serverIp);
		RpcResult result = null;
		try {
			 result =  reference2.propToPom("godzilla", "F:/yixin_fso_app/godzilla", "TEST", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("++|++|++>rpcresult:" + result.getRpcMsg());
	}

	public static Person genPerson() {
		Person person = new Person();
		person.setPersonId("id1");
		person.setLoginName("name1");
		person.setStatus(PersonStatus.ENABLED);

		int sz = Math.max(0, size - 1);
		byte[] attachment = new byte[1024 * sz + 512]; // data size K
		Random random = new Random();
		random.nextBytes(attachment);
		person.setAttachment(attachment);

		ArrayList<Phone> phones = new ArrayList<Phone>();
		Phone phone1 = new Phone("86", "0571", "11223344", "001");
		Phone phone2 = new Phone("86", "0571", "11223344", "002");
		phones.add(phone1);
		phones.add(phone2);

		PersonInfo info = new PersonInfo();
		info.setPhones(phones);
		Phone fax = new Phone("86", "0571", "11223344", null);
		info.setFax(fax);
		FullAddress addr = new FullAddress("CN", "zj", "1234", "Road1", "333444");
		info.setFullAddress(addr);
		info.setMobileNo("1122334455");
		info.setMale(true);
		info.setDepartment("mw");
		info.setHomepageUrl("www.taobao.com");
		info.setJobTitle("dev");
		info.setName("name2");

		person.setInfo(info);

		return person;
	}

	private static void scribblePerson(Person p) {
		p.setStatus(p.getStatus() == PersonStatus.ENABLED ? PersonStatus.DISABLED : PersonStatus.ENABLED);
		p.getAttachment()[0]++;
	}

	private static ThreadLocal<Person> persons = new ThreadLocal<Person>() {
		@Override
		protected Person initialValue() {
			return genPerson();
		}
	};*/
}
