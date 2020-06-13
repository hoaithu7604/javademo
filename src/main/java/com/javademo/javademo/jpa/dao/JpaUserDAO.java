package com.javademo.javademo.jpa.dao;

import com.javademo.javademo.jpa.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class JpaUserDAO {
    @PersistenceContext
    @Qualifier("jpaEntityManagerFactory")
    private EntityManager em;

    public JpaUserDAO() {
    }

    public void addUser(User user) {
        em.persist(user);
    }
    public User findByUsername(String username) {
        return em.find(User.class, username);
    }

    public List<User> getAll(){
        return em.createQuery("select u from "+ User.class.getName()+" u").getResultList();
    }

    public List<User> get(String username, String role, String firstname, String lastname, String sex, String address, String email, String mobilephone)
    {
        String sql = "select u from " + User.class.getName() + " u "+ handleSearchQuery(username,role,firstname,lastname,sex,address,email,mobilephone);
        return em.createQuery(sql).getResultList();
    }

    public User login(String username, String password) {
        try {
            String jpql = "select u from " + User.class.getName() + " u where u.username = :username and u.password = :password";
            Query query = em.createQuery(jpql,User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            User result = (User) query.getSingleResult();
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
            em.merge(entity);
        }
        catch (Exception e){

        }

    }
    public void changPassword(String username, String password){
        try {
            User entity = findByUsername(username);
            entity.setPassword(password);
            em.merge(entity);
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
