package com.example.yangyistarter.repository;

import com.example.yangyistarter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
//在接口上加上mapper注解，让mybatis底层来创建接口的实现类对象，这样就可以直接调用它的方法，
// 自定义的repository类也是通过继承JpaRepository后，直接去使用父类中的方法，感觉是类似的作用，也都是通过调用底层的代码来操作数据库
public interface UserRepository extends JpaRepository<User, BigInteger> {

}
