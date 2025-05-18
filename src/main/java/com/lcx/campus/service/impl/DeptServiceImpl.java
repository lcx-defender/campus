package com.lcx.campus.service.impl;

import com.lcx.campus.domain.Dept;
import com.lcx.campus.domain.Student;
import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.Result;
import com.lcx.campus.domain.vo.TreeSelect;
import com.lcx.campus.enums.DeptStatus;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.mapper.DeptMapper;
import com.lcx.campus.mapper.StudentMapper;
import com.lcx.campus.mapper.TeacherMapper;
import com.lcx.campus.mapper.UserMapper;
import com.lcx.campus.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcx.campus.utils.SecurityUtils;
import com.lcx.campus.utils.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 院校表 服务实现类
 * </p>
 *
 * @author 刘传星
 * @since 2025-03-04
 */
@Service
@Slf4j
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Resource
    private DeptMapper deptMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private StudentMapper studentMapper;

    /**
     * 根据用户id查询用户所在部门
     *
     * @param userId
     * @return 部门信息
     */
    @Override
    public Long getDeptIdByUserId(Long userId) {
        User user = userMapper.selectById(userId);
        String userType = user.getUserType();
        if (userType.equals(UserType.TEACHER.getCode())) {
            return teacherMapper.selectDeptIdByUserId(userId);
        } else if (userType.equals(UserType.STUDENT.getCode())) {
            return studentMapper.selectClassByUserId(userId);
        } else {
            return null;
        }
    }

    /**
     * 判断第一个参数是否是第二个参数的父部门
     *
     * @param deptId1 第一个部门id
     * @param deptId2 第二个部门id
     * @return 部门信息
     */
    @Override
    public boolean isParentDept(Long deptId1, Long deptId2) {
        Dept dept = getById(deptId2);
        if (dept == null) {
            return false;
        }
        if (dept.getParentId() == null || dept.getParentId() == 0) {
            return false;
        }
        // 如果是同一个部门,直接返回true
        if (Objects.equals(deptId1, deptId2)) return true;
        if (dept.getParentId().equals(deptId1)) return true;
        return isParentDept(deptId1, dept.getParentId());
    }

    @Override
    public Long getSelfDeptId() {
        return getDeptIdByUserId(SecurityUtils.getUserId());
    }

    @Override
    public Result treeSelect() {
        // 查询所有部门,去除掉delFlag=1 且 部门的状态已经停用的部门
        List<Dept> depts = list().stream().filter(dept -> dept.getDelFlag().equals("0")).toList();
        return Result.success(buildDeptTreeSelect(depts));
    }

    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<Dept> depts) {
        List<Dept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map((TreeSelect::new)).collect(Collectors.toList());
    }

    @Override
    public List<Dept> buildDeptTree(List<Dept> depts) {
        List<Dept> returnList = new ArrayList<Dept>();
        List<Long> tempList = depts.stream().map(Dept::getDeptId).toList();
        for (Iterator<Dept> iterator = depts.iterator(); iterator.hasNext(); ) {
            Dept dept = (Dept) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }


    @Override
    public Result getDeptInfo(Long deptId) {
        Dept dept = deptMapper.selectById(deptId);
        if (dept == null) {
            return Result.fail("部门不存在");
        }
        return Result.success(dept);
    }

    @Override
    public Result selectDeptTreeList(Dept dept) {
        // 查询所有部门,去除掉delFlag=1的部门
        List<Dept> list = lambdaQuery()
                .eq(Dept::getDelFlag, "0")
                .eq(dept.getDeptName() != null, Dept::getDeptName, dept.getDeptName())
                .eq(dept.getStatus() != null, Dept::getStatus, dept.getStatus())
                .list();
        return Result.success(buildDeptTree(list));
    }

    @Override
    public Result addDept(Dept dept) {
        // 先判断父部门是否存在
        if (dept.getParentId() != null && !isParentDeptExist(dept.getParentId())) {
            return Result.fail("父部门不存在 或 已经删除");
        }
        // 判断当前部门是否存在
        Dept existingDept = lambdaQuery()
                .eq(Dept::getDeptName, dept.getDeptName())
                .eq(Dept::getParentId, dept.getParentId())
                .one();
        if (existingDept != null) {
            if (existingDept.getDelFlag().equals("1")) {
                return Result.fail("部门添加失败,请联系管理员恢复部门");
            }
            return Result.fail("当前部门已存在");
        }
        // 插入新部门
        dept.setCreateTime(LocalDateTime.now());
        save(dept);
        return Result.success("部门添加成功");
    }

    /**
     * 更新部门信息
     *
     * @param dept
     * @return
     */
    @Override
    public Result updateDept(Dept dept) {
        // 先判断父部门是否存在
        if (dept.getParentId() != null && !isParentDeptExist(dept.getParentId())) {
            log.info("父部门ID={}不存在", dept.getParentId());
            return Result.fail("父部门不存在，或已经删除");
        }
        // 判断当前部门是否存在
        Dept existingDept = lambdaQuery()
                .eq(Dept::getDeptName, dept.getDeptName())
                .eq(Dept::getParentId, dept.getParentId())
                .one();
        if (existingDept != null && !existingDept.getDeptId().equals(dept.getDeptId())) {
            if (existingDept.getDelFlag().equals("1")) {
                log.info("部门已存在,当前状态被删除，deptId={}", existingDept.getDeptId());
            }
            log.info("存在相同的部门，deptId={}", existingDept.getDeptId());
            return Result.fail("当前部门已存在");
        }
        if(dept.getStatus().equals(DeptStatus.DISABLED.getCode())){
            // 如果当前部门被禁用,则将所有子部门也禁用
            List<Dept> childDepts = lambdaQuery()
                    .eq(Dept::getParentId, dept.getDeptId())
                    .list();
            for (Dept childDept : childDepts) {
                childDept.setStatus(DeptStatus.DISABLED.getCode());
                updateDeptChildren(childDept);
            }
        } else if(dept.getStatus().equals(DeptStatus.ACTIVE.getCode())){
            // 如果当前部门被启用
            // 先检查是否有祖先部门未被启用，如果有，则不允许启用
            Long parentId = dept.getParentId();
            while(parentId != null && parentId != 0) {
                Dept parentDept = getById(parentId);
                if (parentDept == null || parentDept.getStatus().equals(DeptStatus.DISABLED.getCode())) {
                    return Result.fail("存在当前部门的上级部门未启用，无法启用当前部门");
                }
                parentId = parentDept.getParentId();
            }
        }
        return updateById(dept) ? Result.success("部门修改成功", null) : Result.fail("部门修改失败");
    }

    /**
     * 递归更新dept的子部门状态
     */
    @Override
    public boolean updateDeptChildren(Dept dept) {
        List<Dept> childDepts = lambdaQuery()
                .eq(Dept::getParentId, dept.getDeptId())
                .list();
        for (Dept childDept : childDepts) {
            childDept.setStatus(DeptStatus.DISABLED.getCode());
            if(!updateDeptChildren(childDept)) {
                return false;
            }
        }
        return updateById(dept);
    }

    @Override
    public boolean isParentDeptExist(Long deptId) {
        if (deptId == 0) return true;
        Dept dept = getById(deptId);
        return dept != null && dept.getDelFlag().equals("0");
    }

    /**
     * 删除部门
     */
    @Override
    public Result deleteDept(Long deptId) {
        // 判断当前部门是否存在子部门
        if (hasChildByDeptId(deptId)) {
            return Result.fail("当前部门存在子部门,请先删除子部门");
        }
        // TODO 判断当前部门是否存在用户
        // 逻辑删除
        Dept dept = new Dept();
        dept.setDeptId(deptId);
        dept.setDelFlag("1");
        return updateById(dept) ? Result.success("部门删除成功", null) : Result.fail("部门删除失败");
    }

    /**
     * 判断当前部门是否存在未被删除的子部门
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        // 查询当前部门是否存在子部门
        Long count = lambdaQuery()
                .eq(Dept::getParentId, deptId)
                .eq(Dept::getDelFlag, "0")
                .count();
        return count > 0;
    }

    @Override
    public Result updateDeptStatus(Long deptId, String status) {
        // 判断当前部门是否存在
        Dept dept = getById(deptId);
        if (dept == null) {
            return Result.fail("部门不存在");
        }
        // 更新部门状态
        dept.setStatus(status);
        dept.setUpdateTime(LocalDateTime.now());
        return updateById(dept) ? Result.success("部门状态修改成功") : Result.fail("部门状态修改失败");
    }

    @Override
    public boolean validateDept(Student student) {
        Long universityId = student.getUniversityId();
        Long instituteId = student.getInstituteId();
        Long majorId = student.getMajorId();
        Long classId = student.getClassId();
        // 上下比较是否是父子关系
        if(universityId != null && instituteId != null) {
            if(!isParentDept(universityId, instituteId)) {
                return false;
            }
            if (majorId != null) {
                if (!isParentDept(instituteId, majorId)) {
                    return false;
                }
                if (classId != null) {
                    if (!isParentDept(majorId, classId)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<Dept> list, Dept t) {
        // 得到子节点列表
        List<Dept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Dept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<Dept> getChildList(List<Dept> list, Dept t) {
        List<Dept> tlist = new ArrayList<Dept>();
        Iterator<Dept> it = list.iterator();
        while (it.hasNext()) {
            Dept n = (Dept) it.next();
            if (n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<Dept> list, Dept t) {
        return getChildList(list, t).size() > 0;
    }
}
