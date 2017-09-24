import java.util.*;

public class TrainServiceManagementMain {
    public static void main(String[] args) throws  Exception{
        TrainServiceManagement t=new TrainServiceManagement();
        try {
            Map Output=new TreeMap<Integer,TreeSet<Integer>>();
            List<TrainServiceManagement.TrainDetailsVO> train=new ArrayList<>();
            train=t.getTrainDetails("C:\\Users\\ADMIN\\Documents\\trains.txt",04,20,"2017/10/29");
            Output=t.getTrainSchedule( "C:\\Users\\ADMIN\\Documents\\trains.txt" );
            Set<Integer> set=Output.keySet();
            for(Object i:set) {
                System.out.print( i );

                System.out.println( Output.get( i ) );
            }
            for(TrainServiceManagement.TrainDetailsVO list:train) {
                System.out.println(list);
            }


        } catch (TrainServiceManagement.TrainServiceException e) {
           // throw new TrainServiceManagement.TrainServiceException( "File not found" );
            e.printStackTrace();
        }
    }
}
