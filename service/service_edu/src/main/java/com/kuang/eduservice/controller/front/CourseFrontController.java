package com.kuang.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.common.utils.JwtUtils;
import com.kuang.common.utils.R;
import com.kuang.common.utils.ordervo.CourseWebVoOrder;
import com.kuang.eduservice.client.OrdersClient;
import com.kuang.eduservice.entity.EduCourse;
import com.kuang.eduservice.entity.chapter.ChapterVo;
import com.kuang.eduservice.entity.frontVo.CourseFrontVo;
import com.kuang.eduservice.entity.frontVo.CourseWebVo;
import com.kuang.eduservice.service.EduChapterService;
import com.kuang.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: StarSea99
 * @Date: 2020-10-29 15:02
 */
@Api(description = "课程管理前端")
@RestController
@RequestMapping("/eduservice/coursefront")
//@CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrdersClient ordersClient;

    @ApiOperation(value = "条件查询带分页的课程")
    @PostMapping("getCourseFrontList/{page}/{limit}")
    public R getCourseFrontList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo) {
        Page<EduCourse> pageCourse = new Page<>(page, limit);
        Map<String,Object> map = courseService.getCourseFrontList(pageCourse,courseFrontVo);
        //返回封装的数据
        return R.ok().data(map);
    }

    @ApiOperation(value = "课程详情方法")
    @GetMapping("/getCourseFrontInfo/{courseId}")
    public R getCourseFrontInfo(@PathVariable String courseId, HttpServletRequest request) {
        //根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);

        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        //根据课程id和用户id查询当前课程是否已经支付过
        //报错：feign.FeignException$NotFound: status 404 reading OrdersClient#isBuyCourse(String,String)
        String memberIdToken = JwtUtils.getMemberIdByJwtToken(request);
        boolean buyCourse = ordersClient.isBuyCourse(courseId, memberIdToken);

        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);
    }

    @ApiOperation(value = "根据课程id查询课程信息")
    @PostMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id) {
        CourseWebVo courseInfo = courseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo,courseWebVoOrder);
        return courseWebVoOrder;
    }
}
