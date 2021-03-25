package com.baizhi.service;

import com.baizhi.entity.User;

import java.util.List;

/**
 * 用户(User)表服务接口
 *
 * @author chenyn
 * @since 2020-11-12 17:56:35
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<User> queryAllByLimit(int offset, int limit);


    /**
     * 查询总条数
     * @return 返回总条数
     */
    Long findTotalCounts();


    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);



    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return 返回用户信息
     */
    User findByPhone(String phone);

}