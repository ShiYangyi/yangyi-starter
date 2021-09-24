package com.example.yangyistarter.repository;

import com.example.yangyistarter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
//在接口上加上mapper注解，让mybatis底层来创建接口的实现类对象，这样就可以直接调用它的方法，
// 自定义的repository类也是通过继承JpaRepository后，直接去使用父类中的方法，感觉是类似的作用，也都是通过调用底层的代码来操作数据库
public interface UserRepository extends JpaRepository<User, Long> {

    //下面自定义查询语句中，SELECT后面字段都是在User类中的属性名，FROM后面是类名（与表对应的类），?1表示的是对应方法中传入的第一个参数，如果是?2是传入的第二个参数
    /*@Query(value = " SELECT id, name, password, role FROM User WHERE User.name = ?1 ")
    User findUserByName(String username);*/

    //下面方法写一个声明就可以，但是命名不能随意命名，要类似findById(id)去声明
    Optional<User> findByName(String username);
}
