一、需求分析	6
1.1、系统需求	6
二、 数据库和类图	7
1.数据库关系图	7
2.	类图	9
三、系统实现	13
3.1、界面	13
3.2、系统功能实际	13
四、项目测试数据及结果	23
4.1、登录测试	23
4.2、标签测试	24
4.3、图片测试	25
4.4、博客发表测试	26
4.5、访客浏览测试	27
五、心得体会	30
六、项目安装和部署	30

一、需求分析
1.1、系统需求    
实验名称为《shadow的个人博客》，随着Internet技术的发展，人们的日常生活已经离不开网络。未来社会人们的生活和工作将越来越依赖于数字技术的发展，越来越数字化、网络化、电子化、虚拟化。Internet的发展历程以及目前的应用状况和发展趋势，可以充分地相信网络技术将极大的改变我们的生活和工作方式，甚至社会的价值观也会发生某种变化。
本设计尝试用java web在网络上架构一个个人博客，以使每一位对互联网技术感兴趣的人能够不用出门在家里就能够通过获取互联网技术知识，并且能够是博主的技术知识为更多人看见，达到共同进步的效果。本文从理论和实践两个角度出发，对一个个人博客进行设计与实现分析。论文首先较为详尽地介绍了面向对象分析与设计的有关概念与技术，特别深入介绍了在本系统中运用到的知识，如：面向对象的分析设计流程；系统中所用到的开发技术spring boot以及mybiats-plus的特点、结构框架以及使用方法。接着对个人博客的可行性进行了分析，然后对系统的设计思想、设计目标与系统的整体结构进行了明确的规划。最后对系统的主要页面、数据库、应用程序的设计与实现作了较为详细的讲解。
二、数据库和类图
1.数据库关系图

img
id	主键字段
Local_img	图片在本地位置，用于删除图片时确定图片位置
Virtual_img	图片访问地址，用户访问图片和写博文时引用图片
Tag_id	图片标签，关联于tag表中的id，用于图片分类
Name	图片名称
Description	图片描述

Type
id	主键字段
Name	分类名称


Tag
id	主键字段
Name	标签名称

Blog_tag
Blog_id	博客主键id
Tag_id	标签主键id

Comment
id	主键字段
nickname	评论人名称
Email	邮件地址
Avatar	评论人头像
Create_time	评论创建时间
Update_time	评论更新时间
Blog_id	博客id
Parent_id	父评论id
Admin_comment	是否为博主评论




2.类图

Controller类

Mapper


Poju



Service


Util

三、系统实现
3.1、界面
系统界面是html5 网页端  使用了 jquery  css Semantic UI框架 thymeleaf模板。

3.2、系统功能实现
3.2.1 interceptor拦截器 
由于后台功能肯定要登录才能编辑管理博客，所以需要拦截器管理用户登录和后台访问。
代码如下：
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }
}
3.2.2 dao数据库操作实现

BlogMapper 
public interface BlogMapper extends BaseMapper<Blog> {
    @Select("select year(update_time) as year from blog b group by year(update_time) order by year desc ")
    List<String> findGroupYear();
    @Select("select * from blog where id in (select blog_id from blog_tag where tag_id = #{tagId})")
    Page<Blog> listPageByTagId(Page<Blog> page, Integer tagId);
}

BlogTagMapper
public interface BlogMapper extends BaseMapper<Blog> {
    @Select("select year(update_time) as year from blog b group by year(update_time) order by year desc ")
    List<String> findGroupYear();
    @Select("select * from blog where id in (select blog_id from blog_tag where tag_id = #{tagId})")
    Page<Blog> listPageByTagId(Page<Blog> page, Integer tagId);
}

TagMapper
public interface TagMapper extends BaseMapper<Tag> {
    @Select("select id,name from tag where id in (select tag_id from (select tag_id from blog_tag group by tag_id order by count(*) desc limit #{size} ) as  t)")
    public List<Tag> listTop(Integer size);
    @Select("select id,name from tag where id in (select tag_id from img where tag.id = tag_id) limit #{size}")
    public List<Tag> listImgTop(Integer size);
}

TypeMapper
public interface TypeMapper extends BaseMapper<Type> {
    @Select("select id,name from type where id in (select type_id from (select type_id from blog group by type_id order by count(*) desc limit #{size}) as t)")
    public List<Type> listTop(Integer size);
}

CommentMapper,ImgMapper,userMapper继承mybaits-plus提供的BaseMapper,实现了对相应的数据库表的操作。

3.2.3 service业务

BlogServiceImpl 主要针对博客的编写，修改，删除，查看进行操作，并将相关联的表中的数据，如评论进行整合返回给前端，将markdown格式的文章转换成html文档，并进行分页操作。

@Override
public Blog getAndConvert(Integer id) {
    Blog blog = setBlogInfo(blogMapper.selectById(id));
    if(blog == null){
        throw  new NotFoundException("该博客不存在");
    }
    Blog b = new Blog();
    BeanUtils.copyProperties(blog,b);
    String content = b.getContent();
    b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
    blog.setViews(blog.getViews()+1);
    blogMapper.updateById(blog);
    return b;
}
@Override
public Blog getAndConvert() {
    Blog blog = setBlogInfo(blogMapper.selectOne(new QueryWrapper<Blog>()
            .like("title","关于本站")));
    if(blog == null){
  throw  new NotFoundException("该博客不存在");
    }
    Blog b = new Blog();
    BeanUtils.copyProperties(blog,b);
    String content = b.getContent();
    b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
    blog.setViews(blog.getViews()+1);
    blogMapper.updateById(blog);
    return b;
}

CommentServiceImpl主要对评论进行增删改查操作，并将评论的树级结构转化了两级扁平化结构。
@Service
public class CommentServiceImpl implements CommentService {
    private List<Comment> eachComment(List<Comment> comments) {
        combineChildren(comments);
        return comments;
    }
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            getCommentInfo(replys1);
            for(Comment reply1 : replys1) {
                recursively(reply1);
            }
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }
    private List<Comment> tempReplys = new ArrayList<>();
    private void recursively(Comment comment) {
        getCommentInfo(comment);
        tempReplys.add(comment);//顶节点添加到临时存放集合

        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            getCommentInfo(replys);
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }
}

ImgServiceImpl 主要用于图片的操作。
@Service
public class ImgServiceImpl implements ImgService {
    @Override
    public Page<Img> getImg(Page<Img> page, Integer tagId) {
        if(page == null){
            page = new Page<>(1, 20);
        }
        Page<Img> imgs = imgMapper.selectPage(page, new QueryWrapper<Img>()
                .eq("tag_id", tagId).orderByDesc("id"));
        return imgs;
    }
    @Override
    public List<Tag> listTag(Integer size) {
        List<Tag> tags = tagMapper.listImgTop(size);
        for(int i=0;i<tags.size();i++){
            tags.get(i).setNumber(imgMapper.selectCount(new QueryWrapper<Img>()
                    .eq("tag_id",tags.get(i).getId())));
        }
        return tags;
    }
}

UserServiceImpl主要使用MD5算法加密密码，前端使用MD5算法加密密码，在这里使用MD5算法解密密码并和数据库中的密码进行对比。
@Service
public class UserServiceImpl implements UserService {
    public User checkUser(String username,String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username",username);
        User res = userMapper.selectOne(new QueryWrapper<User>().allEq(map));
        if(res == null)
            return null;
        if(MD5Utils.convertMD52(password)
.equals(MD5Utils.convertMD52(res.getPassword())))
            return res;
        return null;
    }
}

TagServiceImpl和TypeServiceImpl主要用于对标签和分类的增删改查操作，这里不在给与代码示例。
3.2.3 controller 控制web层

后台：
BlogController部分代码

ImageController部分代码

LoginController部分代码




前台：


CommentController部分代码

IndexController部分代码

四、项目测试数据及结果
4.1、登录测试




4.2、标签测试








4.3、图片测试




4.4、博客发表测试



4.5、访客浏览测试









五、心得体会
在本次课程中我学会了如何使用spingboot,mybatis-plus。
1.通过这次软件工程课程设计的实践训练，进一步掌握软件工程的方法和技术，提高软件开发的实际能力，培养工程设计能力和综合分析、解决问题的能力。学习和实践了分析和设计软件系统的各种知识，包括面向对象的系统分析与设计，编码和spring ajax。
2.本系统基本实现了关键的功能模块，在功能上基本满足了用户的需求，但是由于时间较紧，有些模块以及整个系统还有许多不完善的地方，如初始化不太完善，图片上传不太流畅等。
六、项目安装和部署
1.数据库配置-mysql8
在数据库中创建blog数据库，在控制台中输入mysql -uroot -p -Dblog<blog.sql(blog.sql是本文件夹下的数据库脚本文件，如果控制台不在该项目文件夹下，请注意blog.sql文件的位置)。
2.本项目提供项目jar包，可以通过控制台进入jar包所在目录输入 java -jar blog-0.0.1-SNAPSHOT.jar启动本项目。在启动项目前，请修改application.yaml文件中数据库的配置，图片存储路径和服务器端口，如下：
url: 
    jdbc:mysql://localhost:你的数据库端口/blog?userUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
    username: 你的数据库用户名
    password: 你的数据库密码
server:
  port: 你的项目端口号
url:
  imageUrl: "file: 你的图片存储路径"
3.本项目提供源码，使用idea打开，修改maven配置，修改上述application.yaml配置，然后启动项目。
4.本项目登录账户为hxl,密码为123456，由于属于个人博客，不提供注册功能，并且数据库存储md5加密后的密码，如果想要修改数据密码，可以启动util包下的MD5Utils类，将你需要加密的密码输入，然后将加密后的密码填入数据库中。
5.本博客已经部署到云服务器，欢迎老师访问http://www.yuedong.love/。
