package ru.indoornav.dao.interfaces;

import ru.indoornav.*;
import ru.indoornav.authentificate.UserService;
import ru.indoornav.dao.UserEntity;
import ru.indoornav.location.Coordinate;
import ru.indoornav.location.Feature;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IndoorNavDaoInterface {

    void addUser(String name, String position, int tabelId, String password, int rule);

    void createUser(UserEntity user);

    void createWorkshift(Workshift workshift);

    void closeWorkshift(Workshift workshift, Report report);


    User getUser(int tabelId);

    UserService getSuperuserCredentials();

    UserService getUserCredentials(int tabelId);

    Collection<User> getUsers();

    List<Room> getRooms();

    Workshift getWorkshift(int tabelId);

    Workshift getLastWorkshift(int tabelId);

    List<Workshift> getAllWorkshifts(int tabelId);

    Room getRoomInfo(int roomId);

    Report getReport(int tabelId);

    Report getWorkshiftReport(UUID report_id);

    void addTask(Task task);

    void updateTaskStatus(Task task);
    
    List<Task> getUserTasks(int tabelId);

    List<Task> getAllUserTasks(int tabelId); //Активные и не активные

    List<Task> getWorkshiftTask(UUID workshift_id);

    boolean userExist(int tabelId);

    boolean workshiftOpened(int tabelId);

    void updateWorkerLocation(GeoJsonPath location);

    List<Coordinate> getLocationsList(UUID workshiftUUI);


}
