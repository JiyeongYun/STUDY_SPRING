package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.User;
import com.example.study.model.enumclass.UserStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepositoryTest extends StudyApplicationTests {

    // Dependency Injection (DI)
    @Autowired
    private UserRepository userRepository;

    @Test
    public void create(){
        String account = "Test01";
        String password = "Test01";
        UserStatus status = UserStatus.REGISTERED;
        String email = "Test01@gmail.com";
        String phoneNumber = "010-1111-2222";
        LocalDateTime registeredAt = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "AdminServer";

        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setStatus(status);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setRegisteredAt(registeredAt);

        // account, password, status, email만 들어간 User 객체 생성
        // . 연산자를 통해  객체 생성 가능
        // 기존에는 account, password, status, email만 들어가는 생성자를 만들어야 했음.
        User u = User.builder()
                .account(account)
                .password(password)
                .status(status)
                .email(email)
                .build();

    }

    @Test
    @Transactional
    public void read(){
        User user = userRepository.findFirstByPhoneNumberOrderByIdDesc("010-1111-2222");

        if(user != null){
            user.getOrderGroupList().stream().forEach(orderGroup -> {
                System.out.println("----------------주문묶음------------------");
                System.out.println("수령인: "+ orderGroup.getRevName());
                System.out.println("수령지: "+ orderGroup.getRevAddress());
                System.out.println("총금액: "+ orderGroup.getTotalPrice());
                System.out.println("총수량: "+ orderGroup.getTotalQuantity());

                System.out.println("----------------주문상세------------------");
                orderGroup.getOrderDetailList().forEach(orderDetail -> {
                    System.out.println("파트너사 이름: " + orderDetail.getItem().getPartner().getName());
                    System.out.println("파트너사 카테고리: " + orderDetail.getItem().getPartner().getCategory().getTitle());
                    System.out.println("주문 상품: " + orderDetail.getItem().getName());
                    System.out.println("고객센터 번호: " + orderDetail.getItem().getPartner().getCallCenter());
                    System.out.println("주문의 상태: " + orderDetail.getStatus());
                    System.out.println("도착예정일자: " + orderDetail.getArrivalDate());
                });
            });
        }

        Assert.assertNotNull(user);
    }


    @Test
    @Transactional
    public void update(){
        Optional<User> user = userRepository.findById(2L);
        user.ifPresent(selectUser->{
            selectUser.setAccount("pppp");
            selectUser.setUpdatedAt(LocalDateTime.now());
            selectUser.setUpdatedBy("update method");

            userRepository.save(selectUser);
        });
    }

    @Test
    @Transactional
    public void delete(){
        Optional<User> user = userRepository.findById(1L);

        Assert.assertTrue(user.isPresent());            // true

        user.ifPresent(selectUser->{
            userRepository.delete(selectUser);
        });

        Optional<User> deleteUser = userRepository.findById(1L);
        Assert.assertFalse(deleteUser.isPresent());     // false
    }

//    @DeleteMapping("/api/user")
//    public void delete(@RequestParam Long id ){
//        Optional<User> user = userRepository.findById(id);
//        user.ifPresent(selectUser->{
//            userRepository.delete(selectUser);
//        });
//    }

}
