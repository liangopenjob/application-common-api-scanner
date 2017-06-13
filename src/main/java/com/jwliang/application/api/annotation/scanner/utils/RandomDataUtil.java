package com.jwliang.application.api.annotation.scanner.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @ClassName: RandomDataUtil  
 * @Description: 随机生成不同类型的数据工具类
 * @author: liangjunwei
 * @email: liangjwjob_2014@sina.com
 * @date: 2017年2月23日
 *
 */

public class RandomDataUtil {
	
    private static final String NUMBER_CHAR = "0123456789";  
    private static final String LETTER_CHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";  
    private static final String ALL_CHAR = "0123456789abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
    private static final String[] PHONE_PREFIX = "13,18,15".split(",");
    private static final String[] EMAIL_SUFFIX = "@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");
    private static final char[] FIRST_NAME = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万支柯咎管卢莫经房裘缪干解应宗宣丁贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄加封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲台从鄂索咸籍赖卓蔺屠蒙池乔阴胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍却璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庚终暨居衡步都耿满弘匡国文寇广禄阙东殴殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后红游竺权逯盖益桓公俟上官欧阳人赫皇甫尉迟澹冶政淳太叔正轩辕令狐离闾丘长鲜宇徒亓仉督子颛端木西漆雕壤驷良拓拔夹父粱晋楚法汝鄢涂钦百里南门呼延归海舌微生岳帅缑亢况有琴商牟佘佴伯赏墨哈谯笪年爱佟第五言福家姓续".toCharArray();
    private static final char[] GIRL_NAME = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽".toCharArray();
    private static final char[] BOY_NAME = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘".toCharArray(); 
    private static final String[] ROAD_ADDRESS = "重庆大厦, 黑龙江路, 十梅庵街, 遵义路, 湘潭街, 瑞金广场, 仙山街, 仙山东路, 仙山西大厦, 白沙河路, 赵红广场, 机场路, 民航街, 长城南路, 流亭立交桥, 虹桥广场, 长城大厦, 礼阳路, 风岗街, 中川路, 白塔广场, 兴阳路, 文阳街, 绣城路, 河城大厦, 锦城广场, 崇阳街, 华城路, 康城街, 正阳路, 和阳广场, 中城路, 江城大厦, 顺城路, 安城街, 山城广场, 春城街, 国城路, 泰城街, 德阳路, 明阳大厦, 春阳路, 艳阳街, 秋阳路, 硕阳街, 青威高速, 瑞阳街, 丰海路, 双元大厦, 惜福镇街道, 夏庄街道, 古庙工业园, 中山街, 太平路, 广西街, 潍县广场, 博山大厦, 湖南路, 济宁街, 芝罘路, 易州广场, 荷泽四路, 荷泽二街, 荷泽一路, 荷泽三大厦, 观海二广场, 广西支街, 观海一路, 济宁支街, 莒县路, 平度广场, 明水路, 蒙阴大厦, 青岛路, 湖北街, 江宁广场, 郯城街, 天津路, 保定街, 安徽路, 河北大厦, 黄岛路, 北京街, 莘县路, 济南街, 宁阳广场, 日照街, 德县路, 新泰大厦, 荷泽路, 山西广场, 沂水路, 肥城街, 兰山路, 四方街, 平原广场, 泗水大厦, 浙江路, 曲阜街, 寿康路, 河南广场, 泰安路, 大沽街, 红山峡支路, 西陵峡一大厦, 台西纬一广场, 台西纬四街, 台西纬二路, 西陵峡二街, 西陵峡三路, 台西纬三广场, 台西纬五路, 明月峡大厦, 青铜峡路, 台西二街, 观音峡广场, 瞿塘峡街, 团岛二路, 团岛一街, 台西三路, 台西一大厦, 郓城南路, 团岛三街, 刘家峡路, 西藏二街, 西藏一广场, 台西四街, 三门峡路, 城武支大厦, 红山峡路, 郓城北广场, 龙羊峡路, 西陵峡街, 台西五路, 团岛四街, 石村广场, 巫峡大厦, 四川路, 寿张街, 嘉祥路, 南村广场, 范县路, 西康街, 云南路, 巨野大厦, 西江广场, 鱼台街, 单县路, 定陶街, 滕县路, 钜野广场, 观城路, 汶上大厦, 朝城路, 滋阳街, 邹县广场, 濮县街, 磁山路, 汶水街, 西藏路, 城武大厦, 团岛路, 南阳街, 广州路, 东平街, 枣庄广场, 贵州街, 费县路, 南海大厦, 登州路, 文登广场, 信号山支路, 延安一街, 信号山路, 兴安支街, 福山支广场, 红岛支大厦, 莱芜二路, 吴县一街, 金口三路, 金口一广场, 伏龙山路, 鱼山支街, 观象二路, 吴县二大厦, 莱芜一广场, 金口二街, 海阳路, 龙口街, 恒山路, 鱼山广场, 掖县路, 福山大厦, 红岛路, 常州街, 大学广场, 龙华街, 齐河路, 莱阳街, 黄县路, 张店大厦, 祚山路, 苏州街, 华山路, 伏龙街, 江苏广场, 龙江街, 王村路, 琴屿大厦, 齐东路, 京山广场, 龙山路, 牟平街, 延安三路, 延吉街, 南京广场, 东海东大厦, 银川西路, 海口街, 山东路, 绍兴广场, 芝泉路, 东海中街, 宁夏路, 香港西大厦, 隆德广场, 扬州街, 郧阳路, 太平角一街, 宁国二支路, 太平角二广场, 天台东一路, 太平角三大厦, 漳州路一路, 漳州街二街, 宁国一支广场, 太平角六街, 太平角四路, 天台东二街, 太平角五路, 宁国三大厦, 澳门三路, 江西支街, 澳门二路, 宁国四街, 大尧一广场, 咸阳支街, 洪泽湖路, 吴兴二大厦, 澄海三路, 天台一广场, 新湛二路, 三明北街, 新湛支路, 湛山五街, 泰州三广场, 湛山四大厦, 闽江三路, 澳门四街, 南海支路, 吴兴三广场, 三明南路, 湛山二街, 二轻新村镇, 江南大厦, 吴兴一广场, 珠海二街, 嘉峪关路, 高邮湖街, 湛山三路, 澳门六广场, 泰州二路, 东海一大厦, 天台二路, 微山湖街, 洞庭湖广场, 珠海支街, 福州南路, 澄海二街, 泰州四路, 香港中大厦, 澳门五路, 新湛三街, 澳门一路, 正阳关街, 宁武关广场, 闽江四街, 新湛一路, 宁国一大厦, 王家麦岛, 澳门七广场, 泰州一路, 泰州六街, 大尧二路, 青大一街, 闽江二广场, 闽江一大厦, 屏东支路, 湛山一街, 东海西路, 徐家麦岛函谷关广场, 大尧三路, 晓望支街, 秀湛二路, 逍遥三大厦, 澳门九广场, 泰州五街, 澄海一路, 澳门八街, 福州北路, 珠海一广场, 宁国二路, 临淮关大厦, 燕儿岛路, 紫荆关街, 武胜关广场, 逍遥一街, 秀湛四路, 居庸关街, 山海关路, 鄱阳湖大厦, 新湛路, 漳州街, 仙游路, 花莲街, 乐清广场, 巢湖街, 台南路, 吴兴大厦, 新田路, 福清广场, 澄海路, 莆田街, 海游路, 镇江街, 石岛广场, 宜兴大厦, 三明路, 仰口街, 沛县路, 漳浦广场, 大麦岛, 台湾街, 天台路, 金湖大厦, 高雄广场, 海江街, 岳阳路, 善化街, 荣成路, 澳门广场, 武昌路, 闽江大厦, 台北路, 龙岩街, 咸阳广场, 宁德街, 龙泉路, 丽水街, 海川路, 彰化大厦, 金田路, 泰州街, 太湖路, 江西街, 泰兴广场, 青大街, 金门路, 南通大厦, 旌德路, 汇泉广场, 宁国路, 泉州街, 如东路, 奉化街, 鹊山广场, 莲岛大厦, 华严路, 嘉义街, 古田路, 南平广场, 秀湛路, 长汀街, 湛山路, 徐州大厦, 丰县广场, 汕头街, 新竹路, 黄海街, 安庆路, 基隆广场, 韶关路, 云霄大厦, 新安路, 仙居街, 屏东广场, 晓望街, 海门路, 珠海街, 上杭路, 永嘉大厦, 漳平路, 盐城街, 新浦路, 新昌街, 高田广场, 市场三街, 金乡东路, 市场二大厦, 上海支路, 李村支广场, 惠民南路, 市场纬街, 长安南路, 陵县支街, 冠县支广场, 小港一大厦, 市场一路, 小港二街, 清平路, 广东广场, 新疆路, 博平街, 港通路, 小港沿, 福建广场, 高唐街, 茌平路, 港青街, 高密路, 阳谷广场, 平阴路, 夏津大厦, 邱县路, 渤海街, 恩县广场, 旅顺街, 堂邑路, 李村街, 即墨路, 港华大厦, 港环路, 馆陶街, 普集路, 朝阳街, 甘肃广场, 港夏街, 港联路, 陵县大厦, 上海路, 宝山广场, 武定路, 长清街, 长安路, 惠民街, 武城广场, 聊城大厦, 海泊路, 沧口街, 宁波路, 胶州广场, 莱州路, 招远街, 冠县路, 六码头, 金乡广场, 禹城街, 临清路, 东阿街, 吴淞路, 大港沿, 辽宁路, 棣纬二大厦, 大港纬一路, 贮水山支街, 无棣纬一广场, 大港纬三街, 大港纬五路, 大港纬四街, 大港纬二路, 无棣二大厦, 吉林支路, 大港四街, 普集支路, 无棣三街, 黄台支广场, 大港三街, 无棣一路, 贮水山大厦, 泰山支路, 大港一广场, 无棣四路, 大连支街, 大港二路, 锦州支街, 德平广场, 高苑大厦, 长山路, 乐陵街, 临邑路, 嫩江广场, 合江路, 大连街, 博兴路, 蒲台大厦, 黄台广场, 城阳街, 临淄路, 安邱街, 临朐路, 青城广场, 商河路, 热河大厦, 济阳路, 承德街, 淄川广场, 辽北街, 阳信路, 益都街, 松江路, 流亭大厦, 吉林路, 恒台街, 包头路, 无棣街, 铁山广场, 锦州街, 桓台路, 兴安大厦, 邹平路, 胶东广场, 章丘路, 丹东街, 华阳路, 青海街, 泰山广场, 周村大厦, 四平路, 台东西七街, 台东东二路, 台东东七广场, 台东西二路, 东五街, 云门二路, 芙蓉山村, 延安二广场, 云门一街, 台东四路, 台东一街, 台东二路, 杭州支广场, 内蒙古路, 台东七大厦, 台东六路, 广饶支街, 台东八广场, 台东三街, 四平支路, 郭口东街, 青海支路, 沈阳支大厦, 菜市二路, 菜市一街, 北仲三路, 瑞云街, 滨县广场, 庆祥街, 万寿路, 大成大厦, 芙蓉路, 历城广场, 大名路, 昌平街, 平定路, 长兴街, 浦口广场, 诸城大厦, 和兴路, 德盛街, 宁海路, 威海广场, 东山路, 清和街, 姜沟路, 雒口大厦, 松山广场, 长春街, 昆明路, 顺兴街, 利津路, 阳明广场, 人和路, 郭口大厦, 营口路, 昌邑街, 孟庄广场, 丰盛街, 埕口路, 丹阳街, 汉口路, 洮南大厦, 桑梓路, 沾化街, 山口路, 沈阳街, 南口广场, 振兴街, 通化路, 福寺大厦, 峄县路, 寿光广场, 曹县路, 昌乐街, 道口路, 南九水街, 台湛广场, 东光大厦, 驼峰路, 太平山, 标山路, 云溪广场, 太清路".split(",");
    /**
     * 
     * @title: randomNumberStr 
     * @description: 获取指定长度随机字符串(数字)  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午9:47:11  
     * @param length
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomNumberStr(int length){
    	if(length<=0){
    		return "0";
    	}
    	 StringBuilder sb = new StringBuilder();  
         Random random = new Random();  
         for (int i = 0; i < length; i++) {  
             sb.append(NUMBER_CHAR.charAt(random.nextInt(NUMBER_CHAR.length())));  
         }  
         return sb.toString();
    }
    
    /**
     * 
     * @title: randomLetterStr 
     * @description: 获取指定长度随机字符串(字符)  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午9:51:58  
     * @param length
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomLetterStr(int length){
    	if(length<=0){
    		return "string";
    	}
   	    StringBuilder sb = new StringBuilder();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(LETTER_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));  
        }  
        return sb.toString();
    }
    
    /**
     * 获取指定长度随机字符串 
     * @title: randomLowerLetterStr 
     * @description: 获取指定长度随机字符串(字符小写)  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午9:52:27  
     * @param length
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomLowerLetterStr(int length){
   	   return randomLetterStr(length).toLowerCase();
    }
    
    /**
     * 
     * @title: randomUpperLetterStr 
     * @description: 获取指定长度随机字符串(字符大写)    
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午9:53:17  
     * @param length
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomUpperLetterStr(int length){
       return randomLetterStr(length).toUpperCase();
    }
    
    /**
     * 
     * @title: randomAllCharStr 
     * @description: 获取指定长度随机字符串(数字+字符)  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午9:53:30  
     * @param length
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomAllCharStr(int length){
    	if(length<=0){
    		return "string";
    	}
   	    StringBuilder sb = new StringBuilder();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));  
        }  
        return sb.toString();
    }
    
    /**
     * 
     * @title: randomBooleanValue 
     * @description: 随机返回一个boolean值  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午10:25:41  
     * @param 
     * @return 
     * @return boolean 返回类型 
     * @throws
     */
    public static boolean randomBooleanValue(){
    	boolean[] array = {true, false};
    	Random random = new Random();
		return array[random.nextInt(array.length)];
    }
    
    /**
     * 
     * @title: randomBooleanValue 
     * @description: 从字符串中随机返回一布尔值,booleanStr格式如:true,false  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午10:25:41  
     * @param booleanStr
     * @return 
     * @return boolean 返回类型 
     * @throws
     */
    public static boolean randomBooleanValue(String booleanStr){
    	if(StringUtils.isEmpty(booleanStr)){
    		return false;
    	}else{
    		boolean value = false;
    		String[] array = booleanStr.split(",");
    		Random random = new Random();
    		try {
				value = Boolean.valueOf(array[random.nextInt(array.length)]);
			} catch (Exception e) {
				value = false;
			}
    		return value;
    	}
    }
    
    /**
     * 
     * @title: randomByteValue 
     * @description: 随机生成[0-num)区间整数 ,最大范围[0,128)
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午10:17:21  
     * @param num
     * @return 
     * @return byte 返回类型 
     * @throws
     */
    public static byte randomByteValue(int num){
    	if(num<=0||num>128){
    		return 0;
    	}else{
    		Random random = new Random();
        	return Byte.valueOf(String.valueOf(random.nextInt(num)));
    	}
    }
    
    /**
     * 
     * @title: randomByteValue 
     * @description: 从字符串中随机返回一个整数,byteStr格式如:1,5,7  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com  
     * @time: 2017年2月23日 上午10:25:41  
     * @param byteStr
     * @return 
     * @return byte 返回类型 
     * @throws
     */
    public static byte randomByteValue(String byteStr){
    	if(StringUtils.isEmpty(byteStr)){
    		return 0;
    	}else{
    		byte value = 0;
    		String[] array = byteStr.split(",");
    		Random random = new Random();
    		try {
				value = Byte.valueOf(array[random.nextInt(array.length)]);
			} catch (Exception e) {
				value = 0;
			}
    		return value;
    	}
    }
    
    /**
     * 
     * @title: randomIntValue 
     * @description: 随机生成[0-num)区间整数  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午10:17:21  
     * @param num
     * @return 
     * @return int 返回类型 
     * @throws
     */
    public static int randomIntValue(int num){
    	if(num<=0){
    		return 0;
    	}else{
    		Random random = new Random();
        	return random.nextInt(num);
    	}
    }
    
    /**
     * 
     * @title: randomIntValue 
     * @description: 从数组中随机返回一个整数  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午10:25:05  
     * @param array
     * @return 
     * @return int 返回类型 
     * @throws
     */
    public static int randomIntValue(int[] array){
    	if(null==array||0==array.length){
    		return 0;
    	}else{
    		Random random = new Random();
    		return array[random.nextInt(array.length)];
    	}
    }
    
    /**
     * 
     * @title: randomIntValue 
     * @description: 从字符串中随机返回一个整数,intStr格式如:1,5,7  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午10:25:41  
     * @param intStr
     * @return 
     * @return int 返回类型 
     * @throws
     */
    public static int randomIntValue(String intStr){
    	if(StringUtils.isEmpty(intStr)){
    		return 0;
    	}else{
    		int value = 0;
    		String[] array = intStr.split(",");
    		Random random = new Random();
    		try {
				value = Integer.valueOf(array[random.nextInt(array.length)]);
			} catch (NumberFormatException e) {
				value = 0;
			}
    		return value;
    	}
    }
    
    /**
     * 
     * @title: randomIntValue 
     * @description: 随机生成[min-max]区间整数 (正数区间)
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午10:39:50  
     * @param min
     * @param max
     * @return 
     * @return int 返回类型 
     * @throws
     */
    public static int randomIntValue(int min, int max){
    	int value = 0;
    	if(min<=0||max<=0){
    		value = 0;
    	}else if(min==max){
    		value = min;
    	}else{
    		Random random = new Random();
    		if(max>min){
    			value = min + random.nextInt(max-min+1);
    		}else{
    			value = max + random.nextInt(min-max+1);
    		}
    	}
    	return value;
    }
    
    /**
     * 
     * @title: randomLongValue 
     * @description: 随机返回一个正long类型整数  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午10:45:21  
     * @return 
     * @return long 返回类型 
     * @throws
     */
    public static long randomLongValue(){
    	Random random = new Random();
    	return Math.abs(random.nextLong());
    }
    
    /**
     * 
     * @title: randomLongValue 
     * @description: 从字符串中随机返回一个long整数,longStr格式如:1,5,7    
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月27日 上午10:40:31  
     * @param longStr
     * @return 
     * @return long 返回类型 
     * @throws
     */
    public static long randomLongValue(String longStr){
    	if(StringUtils.isEmpty(longStr)){
    		return 0L;
    	}else{
    		long value = 0L;
    		String[] array = longStr.split(",");
    		Random random = new Random();
    		try {
				value = Long.valueOf(array[random.nextInt(array.length)]);
			} catch (NumberFormatException e) {
				value = 0L;
			}
    		return value;
    	}
    }
    
    /**
     * 
     * @title: randomFloatValue 
     * @description: 随机返回指定精度的float数据
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com
     * @time: 2017年2月23日 上午11:13:18  
     * @param decimal 保留小数位数
     * @return 
     * @return float 返回类型 
     * @throws
     */
    public static float randomFloatValue(int decimal){
    	Random random = new Random();
    	float value = random.nextFloat();
    	String valueStr = String.valueOf(value);
    	if(decimal<=0){
    		value = (int)value;
    	}else{
    		int dotIndex = valueStr.indexOf(".");
    		if(dotIndex+decimal<=valueStr.length()-1){
    			value = Float.valueOf(valueStr.substring(0, dotIndex+decimal+1));
    		}else{
    			value = Float.valueOf(StringUtils.rightPad(valueStr, dotIndex+decimal+1, "0"));
    		}
    	}
    	return value;
    }
    
    /**
     * 
     * @title: randomFloatValue 
     * @description: 随机生成[min-max)区间浮点数 (正数区间)  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午11:23:26  
     * @param min 
     * @param max
     * @param decimal 保留小数位数
     * @return 
     * @return float 返回类型 
     * @throws
     */
    public static float randomFloatValue(float min, float max, int decimal){
    	Random random = new Random();
    	float value = random.nextFloat();
    	min = Math.abs(min);
    	max = Math.abs(max);
    	if(min>max){
    		float temp = min;
    		min = max;
    		max = temp;
    	}
    	if(min==max){
    		value = min;
    	}else{
    		value = min + ((max - min) * random.nextFloat());
    	}
    	String valueStr = String.valueOf(value);
    	if(decimal<=0){
    		value = (int)value;
    	}else{
    		int dotIndex = valueStr.indexOf(".");
    		if(dotIndex+decimal<=valueStr.length()-1){
    			value = Float.valueOf(valueStr.substring(0, dotIndex+decimal+1));
    		}else{
    			value = Float.valueOf(StringUtils.rightPad(valueStr, dotIndex+decimal+1, "0"));
    		}
    	}
    	return value;
    }
    
    /**
     * 
     * @title: randomDoubleValue 
     * @description: 随机返回指定精度的double数据
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午11:13:18  
     * @param decimal 保留小数位数
     * @return 
     * @return double 返回类型 
     * @throws
     */
    public static double randomDoubleValue(int decimal){
    	Random random = new Random();
    	double value = random.nextDouble();
    	String valueStr = String.valueOf(value);
    	if(decimal<=0){
    		value = (int)value;
    	}else{
    		int dotIndex = valueStr.indexOf(".");
    		if(dotIndex+decimal<=valueStr.length()-1){
    			value = Double.valueOf(valueStr.substring(0, dotIndex+decimal+1));
    		}else{
    			value = Double.valueOf(StringUtils.rightPad(valueStr, dotIndex+decimal+1, "0"));
    		}
    	}
    	return value;
    }
    
    /**
     * 
     * @title: randomFloatValue 
     * @description: 从指定返回内随机获取一个float数据  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月28日 上午10:20:35  
     * @param floatStr
     * @param decimal
     * @return 
     * @return float 返回类型 
     * @throws
     */
    public static float randomFloatValue(String floatStr, int decimal){
    	Random random = new Random();
    	float value = random.nextFloat();
    	List<Float> floatList = new ArrayList<Float>();
    	if(StringUtils.isNotEmpty(floatStr)){
    		String[] strArray = floatStr.split(",");
    		for(String str:strArray){
    			floatList.add(Float.valueOf(str));
    		}
    	}
    	if(0!=floatList.size()){
    		value = floatList.get(random.nextInt(floatList.size()));
    	}
    	String valueStr = String.valueOf(value);
    	if(decimal<=0){
    		value = (int)value;
    	}else{
    		int dotIndex = valueStr.indexOf(".");
    		if(dotIndex+decimal<=valueStr.length()-1){
    			value = Float.valueOf(valueStr.substring(0, dotIndex+decimal+1));
    		}else{
    			value = Float.valueOf(StringUtils.rightPad(valueStr, dotIndex+decimal+1, "0"));
    		}
    	}
    	return value;
    }
    
    /**
     * 
     * @title: randomDoubleValue 
     * @description: 随机生成[min-max)区间浮点数 (正数区间)  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 上午11:23:26  
     * @param min 
     * @param max
     * @param decimal 保留小数位数
     * @return 
     * @return double 返回类型 
     * @throws
     */
    public static double randomDoubleValue(double min, double max, int decimal){
    	Random random = new Random();
    	double value = random.nextDouble();
    	min = Math.abs(min);
    	max = Math.abs(max);
    	if(min>max){
    		double temp = min;
    		min = max;
    		max = temp;
    	}
    	if(min==max){
    		value = min;
    	}else{
    		value = min + ((max - min) * random.nextDouble());
    	}
    	String valueStr = String.valueOf(value);
    	if(decimal<=0){
    		value = (int)value;
    	}else{
    		int dotIndex = valueStr.indexOf(".");
    		if(dotIndex+decimal<=valueStr.length()-1){
    			value = Double.valueOf(valueStr.substring(0, dotIndex+decimal+1));
    		}else{
    			value = Double.valueOf(StringUtils.rightPad(valueStr, dotIndex+decimal+1, "0"));
    		}
    	}
    	return value;
    }
    
    /**
     * 
     * @title: randomDoubleValue 
     * @description: 从指定返回内随机获取一个double数据  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月28日 上午10:20:35  
     * @param doubleStr
     * @param decimal
     * @return 
     * @return double 返回类型 
     * @throws
     */
    public static double randomDoubleValue(String doubleStr, int decimal){
    	Random random = new Random();
    	double value = random.nextDouble();
    	List<Double> doubleList = new ArrayList<Double>();
    	if(StringUtils.isNotEmpty(doubleStr)){
    		String[] strArray = doubleStr.split(",");
    		for(String str:strArray){
    			doubleList.add(Double.valueOf(str));
    		}
    	}
    	if(0!=doubleList.size()){
    		value = doubleList.get(random.nextInt(doubleList.size()));
    	}
    	String valueStr = String.valueOf(value);
    	if(decimal<=0){
    		value = (int)value;
    	}else{
    		int dotIndex = valueStr.indexOf(".");
    		if(dotIndex+decimal<=valueStr.length()-1){
    			value = Double.valueOf(valueStr.substring(0, dotIndex+decimal+1));
    		}else{
    			value = Double.valueOf(StringUtils.rightPad(valueStr, dotIndex+decimal+1, "0"));
    		}
    	}
    	return value;
    }
    
    /**
     * 
     * @title: randomChStr 
     * @description: 随机生产指定长度简体中文字符串  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 下午2:33:31  
     * @param len
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomChStr(int len){
    	StringBuilder sb = new StringBuilder();
    	if(len>0){
    		Random random = new Random();
    		for(int i=0;i<len;i++){
                String str = "";
                Integer hightPos = (176 + random.nextInt(39)); //获取高位值
                Integer lowPos = (161 + random.nextInt(93)); //获取低位值
                byte[] bytes = new byte[]{hightPos.byteValue(),lowPos.byteValue()};
                try {
					str = new String(bytes, "GBk");//转成中文
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
                sb.append(str);
            }
    	}
        return sb.toString();
    }
    
    /**
     * 
     * @title: randomIdStr 
     * @description: 随机生成身份证号  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 下午2:49:26  
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomIdStr(){
		// 随机生成省、自治区、直辖市代码 1-2
		String provinces[] = {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82"};
		String province = provinces[new Random().nextInt(provinces.length - 1)];
		// 随机生成地级市、盟、自治州代码 3-4
		String citys[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "21", "22", "23", "24", "25", "26", "27", "28"};
		String city = citys[new Random().nextInt(citys.length - 1)];
		// 随机生成县、县级市、区代码 5-6
		String countys[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38"};
		String county = countys[new Random().nextInt(countys.length - 1)];
		// 随机生成出生年月 7-14
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		date.set(Calendar.DATE, date.get(Calendar.DATE) - new Random().nextInt(365 * 100));
		String birth = dft.format(date.getTime());
		// 随机生成顺序号 15-17
		String no = new Random().nextInt(999) + "";
		// 随机生成校验码 18
		String checks[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9","X"};
		String check = checks[new Random().nextInt(checks.length)];
		// 拼接身份证号码
		String id = province + city + county + birth + no + check;
		return id;
    }
    
    /**
     * 
     * @title: randomPhoneStr 
     * @description: 随机生成手机号码  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 下午6:44:38  
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomPhoneStr(){
    	Random random = new Random();
    	StringBuilder sb = new StringBuilder();
    	sb.append(PHONE_PREFIX[random.nextInt(PHONE_PREFIX.length)]);
    	for(int i=0;i<9;i++){
    		sb.append(random.nextInt(Integer.MAX_VALUE)%10);
    	}
    	return sb.toString();
    }
    
    /**
     * 
     * @title: randomEmailStr 
     * @description: 随机生成邮箱  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 下午3:04:05  
     * @param length
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomEmailStr(){
    	return randomAllCharStr(randomIntValue(6, 10)) + EMAIL_SUFFIX[new Random().nextInt(EMAIL_SUFFIX.length)];
    }
    
    /**
     * 
     * @title: randomChName 
     * @description: 随机生成中文姓名  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 下午7:13:42  
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomChName(){
    	StringBuilder sb = new StringBuilder();
    	Random random = new Random();
    	char first = FIRST_NAME[random.nextInt(FIRST_NAME.length)];
    	sb.append(first);
    	if(random.nextInt(2)==0){//男
    		char second = BOY_NAME[random.nextInt(BOY_NAME.length)];
    		sb.append(second);
    		char third = BOY_NAME[random.nextInt(BOY_NAME.length)];
    		sb.append(third);
    	}else{//女
    		char second = GIRL_NAME[random.nextInt(GIRL_NAME.length)];
    		sb.append(second);
    		char third = GIRL_NAME[random.nextInt(GIRL_NAME.length)];
    		sb.append(third);
    	}
    	return sb.toString();
    }
    
    /**
     * 
     * @title: randomAddress 
     * @description: 随机生成地址信息  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月23日 下午7:25:30  
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomAddress(){
    	StringBuilder sb = new StringBuilder();
    	Random random = new Random();
    	sb.append(ROAD_ADDRESS[random.nextInt(ROAD_ADDRESS.length)])
    	  .append(randomIntValue(1, 150))
    	  .append("号")
    	  .append("-").append(randomIntValue(1, 20))
    	  .append("-").append(randomIntValue(1, 20));
    	return sb.toString();
    }
    
    /**
     * 
     * @title: randomString 
     * @description: 从指定范围内随机获取一个字符串,str格式用逗号隔开,如abc,qwe,qrt  
     * @author: liangjunwei
     * @email: liangjwjob_2014@sina.com   
     * @time: 2017年2月28日 上午10:48:51  
     * @param str
     * @return 
     * @return String 返回类型 
     * @throws
     */
    public static String randomString(String str){
    	String value = "";
    	if(StringUtils.isNotEmpty(str)){
    		String[] strArray = str.split(",");
    		Random random = new Random();
    		value = strArray[random.nextInt(strArray.length)];
    	}
    	return value;
    }
    
    public static void main(String[] args) {
    	System.out.println(randomAddress());
    	System.out.println(randomAddress());
	}
}

