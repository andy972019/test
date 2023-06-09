package com.kuang.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.common.utils.JwtUtils;
import com.kuang.common.utils.R;
import com.kuang.common.utils.commentvo.UcenterMemberVo;
import com.kuang.eduservice.client.UcenterClient;
import com.kuang.eduservice.entity.EduComment;
import com.kuang.eduservice.service.EduCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-01
 */
@Api(description = "评论")
@RestController
@RequestMapping("/eduservice/comment")
//@CrossOrigin
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UcenterClient ucenterClient;

    @ApiOperation(value = "分页查询所有评论")
    @GetMapping("getCommentList/{page}/{limit}")
    public R getCommentList(@PathVariable long page,@PathVariable long limit,String courseId) {
        Page<EduComment> pageParam = new Page<>(page,limit);

        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);

        commentService.page(pageParam,wrapper);
        List<EduComment> commentList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());

        return R.ok().data(map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("saveComment")
    public R saveComment(@RequestBody EduComment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }

        comment.setMemberId(memberId);
        System.out.println("*****"+comment);

        UcenterMemberVo ucenterInfo  = ucenterClient.getInfoUc(memberId);
        System.out.println("===++"+ucenterInfo);

        comment.setNickname(ucenterInfo.getNickname());
        comment.setAvatar(ucenterInfo.getAvatar());

        commentService.save(comment);
        return R.ok();
    }

}

