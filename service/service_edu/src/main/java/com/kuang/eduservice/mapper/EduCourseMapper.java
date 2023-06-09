package com.kuang.eduservice.mapper;

import com.kuang.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.eduservice.entity.frontVo.CourseWebVo;
import com.kuang.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-10-15
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public CoursePublishVo getPublicCourseInfo(String courseId);

    CourseWebVo getBaseCourseInfo(String courseId);
}
