package org.example.usercrud.dao;
import org.example.usercrud.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

    public class UserDaoImpl implements UserDao {
        private final SessionFactory sessionFactory;

        public UserDaoImpl(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        @Override
        public Optional<User> findById(Long id) {
            try (Session session = sessionFactory.openSession()) {
                return Optional.ofNullable(session.get(User.class, id));
            } catch (Exception e) {
                throw new RuntimeException("Failed to find user by id: " + id, e);
            }
        }

        @Override
        public List<User> findAll() {
            try (Session session = sessionFactory.openSession()) {
                Query<User> query = session.createQuery("FROM User", User.class);
                return query.list();
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve all users", e);
            }
        }

        @Override
        public User save(User user) {
            Transaction transaction = null;
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                session.save(user);
                transaction.commit();
                return user;
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw new RuntimeException("Failed to save user", e);
            }
        }

        @Override
        public User update(User user) {
            Transaction transaction = null;
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                session.update(user);
                transaction.commit();
                return user;
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw new RuntimeException("Failed to update user", e);
            }
        }

        @Override
        public void delete(Long id) {
            Transaction transaction = null;
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                User user = session.get(User.class, id);
                if (user != null) session.delete(user);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw new RuntimeException("Failed to delete user", e);
            }
        }
    }

