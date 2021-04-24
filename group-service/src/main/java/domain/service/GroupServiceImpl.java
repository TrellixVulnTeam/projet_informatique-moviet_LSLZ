package domain.service;

import java.util.List;
// arraylist
import java.util.ArrayList;

import domain.model.Group;

import javax.enterprise.context.ApplicationScoped; // ApplicationScoped ~singleton
import lombok.NonNull;

// JPA
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import javax.persistence.EntityManagerFactory;

@ApplicationScoped
public class GroupServiceImpl implements GroupService{
    // TODO: DB + be careful about concurrency
    // private List<Group> groups=new ArrayList<>(); // temporary, no DB for the moment..

    //@PersistenceContext(unitName = "GroupPU") // name is the same as in persistence.xml file
    private EntityManager em;
    private final EntityManagerFactory emFactory;

    /*
    We use null as return when there's an error. The HTTP code associated to them are written in GroupRestService.
    If errors we did not catch, we'll have error 500.. we'll have problems if id's can be null

    If no error, return the group or list of groups.
    */
    public GroupServiceImpl(){

        emFactory = Persistence.createEntityManagerFactory("GroupPU");

    }


    public List<Group> getAllGroups(){
        /*
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Group> criteria = builder.createQuery( Group.class );
        criteria.from(Group.class);
        return em.createQuery( criteria ).getResultList();
        */
        try {
            em = emFactory.createEntityManager();
            return em.createQuery("from Group", Group.class).getResultList();
        }
        finally {
            if ( em != null ) em.close();
            //if ( emFactory != null ) emFactory.close();
            return null;
        }
    }

    // find by ID
    public Group getGroup(@NonNull int id){
        /* Need to find the group then return it, Id's are unique
        if not in the list return null, the Rest Service will take care of returning some HTTP code (404 not found here)
        https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html#find-java.lang.Class-java.lang.Object-
        */
        System.out.println(id);
        return em.find(Group.class, id); // null if not found, 404
    }

    public Group createGroup(@NonNull Group group){
        // TODO: verify how id was init ? in Group class. I think we can always create ? because auto increment
        /*
        if (group.getId() != null) {
            // throw new IllegalArgumentException("Group already exists : " + group.getId());
            return null; // the Rest Service will take care of returning some HTTP code, here CONFLICT 409
        }
        */
        em.persist(group);
        return group;
        /*
        // check if group in list of groups
        for (Group g : groups) {
            if (g.getId().equals(group.getId())) { // comparison of strings
                return null; // the Rest Service will take care of returning some HTTP code, here CONFLICT 409
            }
        }
        groups.add(group);
        return group;
        */
    }

    public Group updateGroup(@NonNull Group group){
        Group g = em.find(Group.class, group.getId());
        if (g == null) {
            // throw new IllegalArgumentException("Instrument does not exist : " + instrument.getId());
            return null; // error 404 not found in the group
        }
        em.merge(group);
        return group;
        /*
        // probably do not want to update the id, only update the name

        // find the group first, then update
        for (Group g : groups) {
            if (g.getId().equals(group.getId())) { // comparison of strings
                return group; // not modifying yet
            }
        }
         */

    }

    public Group deleteGroup(@NonNull int id){
        Group group = em.find(Group.class, id);
        if (group == null) {
            // throw new IllegalArgumentException("Instrument does not exist : " + instrument.getId());
            return null; // group does not exist, return null -> will be HTTP status code 404 not found
        }
        em.remove(group);
        return group;
        /*
        for (int index=0; index < groups.size(); index++) {
            if (groups.get(index).getId().equals(id)) { // comparison of strings
                Group returnedGroup=groups.get(index);
                groups.remove(index);
                return returnedGroup;
            }
        }
        // group does not exist, return null -> will be HTTP status code 404 not found
        return null;
        */
    }
}