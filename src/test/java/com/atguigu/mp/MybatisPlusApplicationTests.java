package com.atguigu.mp;

import com.atguigu.mp.entity.User;
import com.atguigu.mp.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusApplicationTests {


    @Autowired
    private UserMapper userMapper;
    @Test
    public void testSelectList() {
        List<User> list = userMapper.selectList(null);
        list.forEach(System.err::println);
    }

    @Test
    public void testInsert(){
        User user = new User();
        user.setAge(11);
        user.setEmail("aaa@162.com");
        user.setName("Cook");
        int insert = userMapper.insert(user);
        System.out.println("影响行数: " + insert);
        System.out.println("user: " + user);
    }

    @Test
    public void testOptimisticLocker() {
        //查询
        User user = userMapper.selectById(1L);
        //修改数据
        user.setName("Helen Yao test");
        user.setEmail("helentest@qq.com");
        //执行更新
        userMapper.updateById(user);
    }

    /**
     * 测试乐观锁插件 失败
     */
    @Test
    public void testOptimisticLockerFail() {

        //查询
        User user = userMapper.selectById(1L);
        //修改数据
        user.setName("Helen Yao1");
        user.setEmail("helen1@qq.com");

        //模拟另一个线程中间更新了数据
        //查询
        User user2 = userMapper.selectById(1L);
        //修改数据
        user2.setName("Helen Yao2");
        user2.setEmail("helen2@qq.com");
        userMapper.updateById(user2);

        //执行更新
        userMapper.updateById(user);
    }

    @Test
    public void testSelectBatchIds(){

        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    /**
     * map中的key是数据库表中的列名
     */
    @Test
    public void testSelectByMap(){

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Helen");
        map.put("age", 18);
        List<User> users = userMapper.selectByMap(map);

        users.forEach(System.out::println);
    }

    @Test
    public void testSelectMapsPage() {

        Page<User> page = new Page<>(1, 5);

        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(page, null);

        //注意：此行必须使用 mapIPage 获取记录列表，否则会有数据类型转换错误
        mapIPage.getRecords().forEach(System.out::println);
        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    @Test
    public void testSelectPage() {

        Page<User> page = new Page<>(1,5);
        userMapper.selectPage(page, null);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    /**
     * 测试 逻辑删除
     */
    @Test
    public void testLogicDelete() {

        int result = userMapper.deleteById(4L);
        System.out.println(result);
    }

    /**
     * 测试 逻辑删除后的查询：
     * 不包括被逻辑删除的记录
     */
    @Test
    public void testLogicDeleteSelect() {
        User user = new User();
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    public void testDelete() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .isNotNull("name")
                .ge("age", 25)
                .isNotNull("email");
        int result = userMapper.delete(queryWrapper);
        System.out.println("delete return count = " + result);
    }

    @Test
    public void testSelectCount() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("age", 20, 30);

        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println(count);
    }
}
