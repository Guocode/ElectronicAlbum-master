package ea.view.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import ea.base.BaseAction;
import ea.domain.Album;
import ea.domain.AlbumBgp;
import ea.domain.Photo;
import ea.domain.Photo_pro;
import ea.domain.Role;
import ea.domain.User;
import ea.util.ImgCompress;
import ea.util.PageShow;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User>{

	private static final long serialVersionUID = 1L;
	// 对应js里面的fileObjName，而不是input type ="text" 里面的name值,
	//这里也可以不用list，用File uploadify。因为虽然是批量上传，但还是一个一个的处理文件的
	//等一个文件处理完后才会处理第二个
	private File uploadify; 
	private String uploadifyFileName;
	private String uploadifyContentType;
	private static final String savePath = "imgs";// 文件上传后保存的路径
	private String mvUrl;
	private int albumId = 0; //用户请求的相册模板ID
	private Long userid;//标记哪个用户上传
	private Long[] roleIds;
	
	
	private int pageNow=1;   //动态改变 页面获取
    private int pageSize=5;    //固定不变
	
    private int BgppageNow=1;   //动态改变 页面获取
    private int BgppageSize=1;    //固定不变
	
	
    private JSONObject json;			//获取页面传过来的json数据

	/**
	 * ajax 上传图片并返回文件路径
	 * 
	 * @return
	 * @throws Exception
	 */
    public String upload() throws Exception {
		System.out.println("上传照片");
		List<String> mfiles = new ArrayList<String>();
	
		User user=(User)ActionContext.getContext().getSession().get("user");
	
		userid=user.getId();

		//文件重命名，文件保存
		String updatadate=new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String fileName =updatadate+ "_" + uploadifyFileName;
		String sfileName = "s_"+fileName;
		ActionContext ac = ActionContext.getContext();
		ServletContext sc = (ServletContext) ac
				.get(ServletActionContext.SERVLET_CONTEXT);
		String realPath = sc.getRealPath("/");
		String dest = realPath + savePath + "/" + fileName;
		String sdest = realPath + savePath + "/" + sfileName;
		System.out.println("目标路径:"+dest);
		FileUtils.copyFile(uploadify, new File(dest));
		ImgCompress imgCom_m = new ImgCompress(dest,sdest);  
	    imgCom_m.resizeFix(650, 650);  

		mfiles.add(savePath + "/" + fileName);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		// 返回图片地址，这里我们实际用的时候可能返回实际图片和缩略图，
		//上传后真正显示的时候用缩略图,实际保存地址为原图片地址。那样加载更快
		out.write(mfiles.get(0));
		out.close();
		out.flush();
		
		Photo photo=new Photo();
		photo.setPName(fileName);
		photo.setOwnerId(userid);
		photo.setPaddr(dest);
		photo.setUpdatetime(updatadate);
		photoService.save(photo);
		
		System.out.println("mfiles.get(0):"+mfiles.get(0));
		System.out.println("uploadify:"+uploadify);
		System.out.println("uploadifyFileName:"+uploadifyFileName);
		System.out.println("uploadifyContentType:"+uploadifyContentType);
		return "toPhotoList";
	}

	// 批量保存图片地址

	public String savePath() throws IOException {
		System.out.println("mvUrl:"+mvUrl);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.write("保存成功");
		out.close();
		out.flush();
		return null;
	}
	
	
	/**
	 * 用户首页
	 */
	public String index() throws Exception{
		return "index";
	}
	
	
	/** 列表*/
	public String list() throws Exception{
		List<User> userList=userService.findAll();
		ActionContext.getContext().put("userList", userList);
		return "list";
	}
	
	/** 删除*/
	public String delete() throws Exception{
		userService.delete(model.getId());
		return "toList";
	}
	
	/** 添加页面*/
	public String addUI() throws Exception{
		
		
		return "add";
	}
	
	/** 管理员添加页面*/
	public String AaddUI() throws Exception{
		
		
		return "Aadd";
	}
	
	/**添加*/
	public String add() throws Exception{
		User user=userService.findByLoginName(model.getLoginName());
		if(user!=null) {
			addFieldError("regist", "用户名已被注册！");
			return "add";
		}
		else if(model.getPassword().equals("")) 
		{
			addFieldError("regist", "密码不能为空！");
			return "add";
		}
		else{
			String md5Digest=DigestUtils.md5Hex(model.getPassword());
			model.setPassword(md5Digest);
			userService.save(model);
			return "loginUI";
		}
	}
	
	/**管理员添加用户*/
	public String Aadd() throws Exception{
//		System.out.println("Aadd+====="+model.getLoginName());
		User user=userService.findByLoginName(model.getLoginName());
		if(user!=null) {
			addFieldError("regist", "用户名已被注册！");
			return "Aadd";
		}
		String md5Digest=DigestUtils.md5Hex("1234");
		
		model.setPassword(md5Digest);
//		User user=new User();
//		user.setName(model.getName());
//		user.setDescription(model.getDescription());
//		//保存到数据库中
//		userService.save(user);
		
		userService.save(model);
		
		return "toList";
	}
	
	/** 修改页面*/
	public String editUI() throws Exception{
		User user=(User)ActionContext.getContext().getSession().get("user");
		ActionContext.getContext().getValueStack().push(user);
		showalbum();
		return "edit";
	}
	
	/** 修改*/
	public String edit() throws Exception{
		
		//从数据库获取原对象
		User user=(User)ActionContext.getContext().getSession().get("user");
		
		System.out.println("怎么还不成功啊！！！！________________________"+user);
		System.out.println("怎么还不成功啊！！！！________________________"+model);
		//设置修改的属性
		//设置修改的属性
		ActionContext context=ActionContext.getContext();
		Map<String, Object>map=context.getParameters();
		Set<String> keys=map.keySet();
		for(String key:keys) {
			Object[] obj=(Object[]) map.get(key);
			System.out.println(Arrays.toString(obj));
		}
		
		user.setName(model.getName());
		user.setPhoneNumber(model.getPhoneNumber());
		user.setEmail(model.getEmail());
		//保存到数据库
		
		System.out.println("怎么还不成功啊！！！！________________________"+model.getName());
		userService.update(user);
		return "personal";
	}
	
	/**管理员修改用户信息页面*/
	public String AeditUI() throws Exception{
		
		//准备回显数据
		User user=userService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(user);
		//准备数据roleList
		List<Role> roleList=roleService.findAll();
		ActionContext.getContext().put("roleList", roleList);
		
		if(user.getRoles()!=null)
		{
			roleIds=new Long[user.getRoles().size()];
			int index=0;
			for(Role role:user.getRoles()) {
				roleIds[index++]=role.getId();
			}
		}
		
		return "Aedit";
	}
	
	/** 修改*/
	public String Aedit() throws Exception{
		
		//从数据库获取原对象
		User user=userService.getById(model.getId());
		
//		System.out.println("怎么还不成功啊！！！！________________________"+user);
		//设置修改的属性
		user.setName(model.getName());
		user.setGender(model.getGender());
		user.setPhoneNumber(model.getPhoneNumber());
		user.setEmail(model.getEmail());
		user.setDescription(model.getDescription());
		//保存到数据库
		
		List<Role> roleList=roleService.getByIds(roleIds);
		user.setRoles(new HashSet<Role>(roleList));
		
//		System.out.println("怎么还不成功啊！！！！________________________"+model.getName());
		userService.update(user);
		return "toList";
	}
	
	
	/**初始化密码功能*/
	
	public String initPassword()throws Exception{
		//从数据库获取原对象
		User user=userService.getById(model.getId());
		
		//设置修改的属性
		String md5Digest=DigestUtils.md5Hex("1234");
		user.setPassword(md5Digest);
		
		//保存到数据库
		userService.update(user);
		
		return "toList";
	}

	
	
	/**登录页面*/
	public String loginUI() throws Exception{
		return "loginUI";
	}
	
	/**登录*/
	public String login() throws Exception{
		User user=userService.findByLoginNameAndPassword(model.getLoginName(),model.getPassword());
		if(user==null) {
			addFieldError("login", "用户名或密码不正确！");
			return "loginUI";
		}
		else {
			//登录用户
			ActionContext.getContext().getSession().put("user", user);
			return "toIndex";
		}		
		
	}
	
	
	
	/**注销*/
	public String logout() throws Exception{
		ActionContext.getContext().getSession().remove("user");
		return "logout";
	}
	
	
	/**
	 * 用户进入个人中心
	 */
	public String personal() throws Exception{
		
		return "personal";
	}
	
	
	/**
	 * 收藏某个相册
	 * @return
	 */
	public String addalbum()throws Exception{
		User user=(User) ActionContext.getContext().getSession().get("user");
//		System.out.println("Addalbum:user+++++++++++"+user);
//		System.out.println("Addalbum:model.id+++++++++++"+model.getId());
		Album album=albumService.getById(model.getId());
//		System.out.println("Addalbum:album+++++++++++"+album);
		Set<Album> albums=user.getAlbums();
//		System.out.println("Addalbum:albums+++++++++++"+user.getAlbums());
		int sign=0;
		for(Album a:albums)
		{
			if(a.getId()==model.getId())
			{
				sign=1;
				break;
			}
		}
		if(sign!=1)
		{
			albums.add(album);
			user.setAlbums(albums);
			userService.update(user);
		}
		return "edit";
	} 
	
	/**
	 * 展示用户收藏的相册
	 * @return
	 */
	public String showalbum()throws Exception{
		User user=(User) ActionContext.getContext().getSession().get("user");
//		System.out.println("Showalbum:user++++++++++"+user);		
//		List<Album> userAlbums= albumService.findAlbumByUserid(user.getId());	
		
		
		Set<Album> albums=user.getAlbums();
		List<Album> userAlbums=new ArrayList<Album>(albums);
		System.out.println("Showalbum:userAlbums++++++++++"+userAlbums);
		ActionContext.getContext().put("userAlbums", userAlbums);
		return "showalbumok";
	}
	
	/**
	 * 删除某个收藏的相册
	 * @return
	 * 
	 */
	public String delAlbum()throws Exception{
//		System.out.println("DelAlbum:model++++++++++++++"+model.getId());
		User user=(User) ActionContext.getContext().getSession().get("user");
		Set<Album> albums=user.getAlbums();
		Album album=albumService.getById(model.getId());
//		System.out.println("DelAlbum:model++++++++++++++"+model);
		
		for(Album a:albums) {
//			System.out.println("----------"+a.getId());
			if(a.getId()==album.getId())
				albums.remove(a);
		}
		
//		System.out.println("DelAlbum:albums++++++++++++++"+albums.toString());
		user.setAlbums(albums);
//		System.out.println("DelAlbum:user++++++++++++++"+user.getId());
		userService.update(user);

		return "edit";
	}
	
	/**
	 * 分页展示照片
	 * @return
	 */
	public String getAllPhotos() {
		User user=(User) ActionContext.getContext().getSession().get("user");
		System.out.println("getAllBgps:user++++++++++++"+user.getId());
		System.out.println("getAllBgps:page++++++++++++"+getPageNow());

		List<Photo> photoList=photoService.findAllPhotosByUserid(pageNow,pageSize,user.getId());
		if(photoList.size()>0) {   //bgp列表
			ActionContext.getContext().put("photoList", photoList);
			PageShow page=new PageShow(pageNow,photoService.findPhotoSizeByUserid(user.getId()),pageSize);

			ActionContext.getContext().getSession().put("pagePhoto", page);
		
		}
		
//		System.out.println("getAllbgps:+++++++++++++++over");
		return "list";
	}
	
	
	/**
	 * 用户制作一个相册
	 * @return
	 */
	public String makeAlbum()throws Exception{
//		if(model.getId()!=null)
//		{
//			System.out.println("makeAlbum++++++++++"+model.getId());
////			List<AlbumBgp> modelBgps=albumService.findAlbumBgpsByAlbumid(BgppageNow, BgppageSize, model.getId());
////			System.out.println("MakeAlbum+++++++++++"+modelBgps);
////			if(modelBgps.size()>0)
////			{
////				ActionContext.getContext().put("modelBgps", modelBgps);
////				PageShow page=new PageShow(BgppageNow,albumService.findAlbumBgpSizeByAlbumid(model.getId()),BgppageSize);
////
////				ActionContext.getContext().put("pageAlbumBgp", page);
////			}
//			
//			List<AlbumBgp> modelBgps=albumService.findAllbgByAlbumId(model.getId());
//			if(modelBgps.size()>0)
//				ActionContext.getContext().put("modelBgps", modelBgps);
//		}
//		getAllPhotos();
//		Templist();	
//		System.out.println("MakeAlbum:model:================="+getAlbumId());
		List<AlbumBgp> modelBgps=albumService.findAllbgByAlbumId((long)this.getAlbumId());
		ActionContext.getContext().put("modelBgps", modelBgps);
		Templist();
//		System.out.println("MakeAlbum:albumbgps++++++++++++++++++"+modelBgps);
		return "makeAlbum";
		
	}
	
	
	/**
	 * 形成相册
	 */
	public String beAlbum() throws Exception{
		//获取页面传送过来的json数据
		//Photo_pro photo_pro=(Photo_pro)JSONObject.toBean(json, Photo_pro.class);
        HttpServletRequest req = ServletActionContext.getRequest();
        StringBuilder sb = new StringBuilder();  
        try(BufferedReader reader = req.getReader();) {  
             char[]buff = new char[1024];  
             int len;  
             while((len = reader.read(buff)) != -1) {  
                      sb.append(buff,0, len);  
             }  
        }catch (IOException e) {  
                 e.printStackTrace();  
        }  
        String sb2 = URLDecoder.decode(sb.toString(),"UTF-8");
        /*
         * 解码前：mydata=%7B%22name%22%3A%22ji%22%2C%22age%22%3A20%7D 
         * 解码后：mydata={"name":"ji","age":20}
         */
        
        /*
         * 存储使用了哪个模板
         * 为用户新建一个样例对象并存储
         */
        JSONArray jsonArray = JSONArray.fromObject(sb2);
        System.out.println(getAlbumId());
        System.out.println(sb2);
		System.out.println(jsonArray);
		return "success";
	}
	
	
	
	/**
	 * 展示相册模板
	 * @return
	 */
	/** 列表*/
	public String Templist() throws Exception{
		List<Album> albumList=albumService.findAll();
		ActionContext.getContext().put("albumList", albumList);
//		System.out.println("Templist:+++++++++++++++over");
		return "list";
	}
	
	
	
	//----------------各属性set和get方法
	
	public File getUploadify() {
		return uploadify;
	}

	public void setUploadify(File uploadify) {
		this.uploadify = uploadify;
	}

	public String getUploadifyFileName() {
		return uploadifyFileName;
	}

	public void setUploadifyFileName(String uploadifyFileName) {
		this.uploadifyFileName = uploadifyFileName;
	}

	public String getUploadifyContentType() {
		return uploadifyContentType;
	}

	public void setUploadifyContentType(String uploadifyContentType) {
		this.uploadifyContentType = uploadifyContentType;
	}


	public String getMvUrl() {
		return mvUrl;
	}

	public void setMvUrl(String mvUrl) {
		this.mvUrl = mvUrl;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getSavepath() {
		return savePath;
	}

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getBgppageNow() {
		return BgppageNow;
	}

	public void setBgppageNow(int bgppageNow) {
		BgppageNow = bgppageNow;
	}

	public int getBgppageSize() {
		return BgppageSize;
	}

	public void setBgppageSize(int bgppageSize) {
		BgppageSize = bgppageSize;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	

	
}
