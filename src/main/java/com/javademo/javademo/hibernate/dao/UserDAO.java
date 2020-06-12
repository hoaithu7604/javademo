package com.javademo.javademo.hibernate.dao;

import com.javademo.javademo.hibernate.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class UserDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    public User findByUsername(String username) {
        return currentSession().get(User.class, username);
    }

    public void addUser(User user) {
        Serializable s = currentSession().save(user);
    }

    public List<User> getAll() {
        String hql = "from "+User.class.getName();
        Query<User> query = currentSession().createQuery(hql,User.class);
        return query.list();
    }

    public List<User> get(String username, String role, String firstname, String lastname, String sex, String address, String email, String mobilephone)
    {
        String hql = "FROM " + User.class.getName() + handleSearchQuery(username,role,firstname,lastname,sex,address,email,mobilephone);
        Query<User> query = currentSession().createQuery(hql, User.class);
        return query.getResultList();
    }

    public User login(String username, String password){
        try {
            String hql = "FROM " + User.class.getName() + " where username = :username and password = :password";
            Query<User> query = currentSession().createQuery(hql, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            User result = query.getSingleResult();
            return result;
        }
        catch(Exception e){
            return null;
        }
    }

    public void update(String username, String firstname, String lastname, String sex, String address, String email, String mobilephone){
        try {
            User entity = findByUsername(username);
            entity.setFirstName(firstname);
            entity.setAddress(address);
            entity.setEmail(email);
            entity.setLastName(lastname);
            entity.setMobilePhone(mobilephone);
            entity.setSex(sex);
            currentSession().update(entity);
        }
        catch (Exception e){

        }

    }

    public void changPassword(String username, String password){
        try {
            User entity = findByUsername(username);
            entity.setPassword(password);
            currentSession().update(entity);
        }
        catch (Exception e){

        }

    }

    private String handleSearchQuery(String username, String role, String firstname, String lastname, String sex, String address, String email, String mobilephone){
        String query = "";
        List<String> statement = new ArrayList<String>();

        if (!("".equals(username) &&  "0".equals(role) &&  "".equals(firstname) &&  "".equals(lastname)
                &&  "".equals(sex) &&  "".equals(address) &&  "".equals(email) &&  "".equals(mobilephone)
        )){

            if(!"".equals(username)){
                statement.add("username LIKE '%" + username + "%'");
            }
            if(!"".equals(firstname)){
                statement.add("firstname LIKE '%" + firstname + "%'");
            }
            if(!"".equals(lastname)){
                statement.add("lastname LIKE '%" + lastname + "%'");
            }
            if(!"".equals(sex)){
                statement.add("sex LIKE '%" + sex + "%'");
            }
            if(!"".equals(address)){
                statement.add("address LIKE '%" + address + "%'");
            }
            if(!"".equals(email)){
                statement.add("email LIKE '%" + email + "%'");
            }
            if(!"".equals(mobilephone)){
                statement.add("mobilephone LIKE '%" + mobilephone + "%'");
            }
            if(!"0".equals(role)){
                statement.add("groupid = " + role);
            }

            query = query.concat("  WHERE ");
            query = query.concat(statement.get(0));
            for(int i = 1; i< statement.size(); i++){
                query = query.concat("  AND  ");
                query = query.concat(statement.get(i));
            }
        }

        return query;
    }
}
