package cn.godzilla.common;

/**
 * Hello world!
 *
 */
public class App 
{
	/**
	 * 生成唯一  冲突分支 url
	 * @return
	 */
	private static String getRandConflictURL(String trunkUrl) {
		String uuid = StringUtil.getRandom(6);
		String baseUrl = trunkUrl.substring(0, trunkUrl.indexOf("/trunk"));
		System.out.println(baseUrl);
		String conflict_url = baseUrl + "/conflict";
		System.out.println(conflict_url);
		return null;
	}
	public static void main(String args[]){
		getRandConflictURL("http://svn.caiwu.corp/svn/fso-java/gardener/trunk/gardener");
	}
}
