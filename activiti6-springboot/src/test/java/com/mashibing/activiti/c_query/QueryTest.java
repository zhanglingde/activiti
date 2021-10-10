package com.mashibing.activiti.c_query;

import com.mashibing.activiti.ApplicationTests;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p></p>
 *
 * @author 孙志强
 * @date 2020-06-27 18:58
 */
public class QueryTest extends ApplicationTests {

    @Resource
    private IdentityService identityService;

    @Test
    public void insert() {
        for (int i = 0; i < 10; i++) {
            User user = identityService.newUser(String.valueOf(i));
            user.setFirstName("张三" + i);
            user.setEmail("邮件" + i);
            identityService.saveUser(user);
        }
    }
    /*查询list*/
    @Test
    public void querySingle() {
        User user = identityService.createUserQuery()
                .singleResult();
        System.out.println(user);
    }
    /*查询list*/
    @Test
    public void queryList() {
        List<User> list = identityService.createUserQuery()
                .list();
        if (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                System.out.println(user.getFirstName() + "\t" + user.getEmail());
            }
        }
    }
    /*查询分页*/
    @Test
    public void querypage() {
        List<User> list = identityService.createUserQuery().listPage(2, 5);
        if (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                System.out.println(user.getFirstName() + "\t" + user.getEmail());
            }
        }
    }
    /*查询数量*/
    @Test
    public void queryPage() {
        long count = identityService.createUserQuery().count();
        System.out.println(count);
    }
    /*一个字段排序*/
    @Test
    public void querySingleSort() {
        List<User> list = identityService.createUserQuery().orderByUserId().asc().list();
        if (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                System.out.println(user.getFirstName() + "\t" + user.getEmail());
            }
        }
    }

    /*多个字段排序 注意一点：有一个order by 就有一个升降序，否则后者会对前者覆盖*/
    @Test
    public void queryMultiSort() {
        UserQuery userQuery = identityService.createUserQuery().orderByUserId();
        List<User> list = userQuery.desc().orderByUserEmail().asc().list();
        if (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                System.out.println(user.getFirstName() + "\t" + user.getEmail());
            }
        }
    }

    /*根据条件*/
    @Test
    public void queryCondition() {
        String firstName = "张三1";
        String firstNameLike = "张三";
//        List<User> list = identityService.createUserQuery().userFirstName(firstName).list();
        List<User> list = identityService.createUserQuery().userFirstNameLike("%"+firstNameLike+"%").list();
        if (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                System.out.println(user.getFirstName() + "\t" + user.getEmail());
            }
        }
    }

    /*自定义查询*/
    @Test
    public void queryNative() {
        String sql = "select  * from act_id_user1";
        List<User> list = identityService.createNativeUserQuery().sql(sql).list();
        if (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                System.out.println(user.getFirstName() + "\t" + user.getEmail());
            }
        }
    }


}
