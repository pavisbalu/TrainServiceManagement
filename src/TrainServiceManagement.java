import javax.print.attribute.standard.Destination;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.util.TreeSet;


public class TrainServiceManagement {

    FileReader fileReader=null;
    BufferedReader bufferReader=null;
    final int TR1Min=1;
    final int TR1Max=10;
    final int TR2Min=11;
    final int TR2Max=20;
        /* Return the list of trains for the given parameter */
        public List<TrainDetailsVO> getTrainDetails(final String filePath, int source,
                                                    int destination, String dateOfTravel)
                throws TrainServiceException {

if(source<TR1Min || destination>TR2Max) throw new TrainServiceException( "Out of boundaries" );

if (source==destination) throw new TrainServiceException( "S/d not same" );
TrainDetailsVO trainDetailsVO=null;
List<TrainDetailsVO> TrainDetailsList=new ArrayList<TrainDetailsVO>();
SimpleDateFormat formatter=null;
formatter=new SimpleDateFormat( "yyyy/MM/dd" );
formatter.setLenient( false );
int day=0;
char special='Y';
Date DateOfTravel;
            DateOfTravel = new Date();
            try {
                DateOfTravel=formatter.parse( dateOfTravel );
            } catch (ParseException e) {
                throw new TrainServiceException( "Date invalid" );
            }
if(DateOfTravel.compareTo( new Date())<0) throw new TrainServiceException( "Date invalid" );
            Calendar c=Calendar.getInstance();
            c.setTime( DateOfTravel );
            day=c.get( Calendar.DAY_OF_WEEK );
            special=((day==1)?'Y':'N');
            String curuLine;
            curuLine = null;
            String[] tempArr=null;
            try {
                FileReader file=new FileReader(filePath);
                BufferedReader bufferReader=new BufferedReader(file);

                while ((curuLine=(bufferReader.readLine()))!=null)

                {
                    trainDetailsVO=new TrainDetailsVO();
                    tempArr=curuLine.split( "," );
                    int tempSrc= Integer.parseInt(tempArr[2]);
                    int tempDest=Integer.parseInt(tempArr[3]);
                    char tempSpecial=tempArr[4].toCharArray()[0];
                    char fullSearch='Y';
                    if(special=='Y')fullSearch='N';else fullSearch='Y';
                    if((tempSrc==source && tempDest==destination && fullSearch=='Y')?true:(tempSpecial==special)){
                        trainDetailsVO = new TrainDetailsVO();
                        trainDetailsVO.setSource(tempSrc);
                        trainDetailsVO.setDestination(tempDest);
                        trainDetailsVO.setDateOfTravel(DateOfTravel);
                        trainDetailsVO.setSpecial(tempSpecial);
                        trainDetailsVO.setRoute(tempArr[1]);
                        trainDetailsVO.setTrainNumber(tempArr[0]);
                        TrainDetailsList.add(trainDetailsVO);

                    }



                }
            } catch (IOException e) {
                throw new TrainServiceException( "Notvalid" );
            }

            for(TrainDetailsVO l:TrainDetailsList) {
                System.out.println(l.getTrainNumber());
                System.out.println(l.getDateOfTravel());
                System.out.println(l.getDestination());
                System.out.println(l.getSource());
                System.out.println(l.getRoute());
                System.out.println(l.getSpecial());
            }
            return TrainDetailsList;
        }

        /* Return the special trains */
        public Map getTrainSchedule(String filePath) throws TrainServiceException {

            String currLine=null;
            TreeSet<Integer> Trains=new TreeSet<Integer>();
            TreeMap<Integer,TreeSet<Integer>> T=new TreeMap<Integer,TreeSet<Integer>>();
            String[] tempArray=null;
            try {
                fileReader=new FileReader(filePath);
                bufferReader=new BufferedReader(fileReader);
                try {
                    while((currLine=(bufferReader.readLine()))!=null){
                        tempArray=currLine.split(",");
                        if(tempArray[4].equals("Y"))

                            Trains.add( Integer.parseInt( tempArray[0] ) );

                    }
                } catch (IOException e) {
                    throw  new TrainServiceException("input failed");
                }
            } catch (FileNotFoundException e) {
                throw new TrainServiceException("File Not Found");
            }

T.put( 1,Trains );
            //Write the code here
return T;
        }



    /* Train Detail Value Object - DO NOT CHANGE*/
    class TrainDetailsVO {
        private String trainNumber;
        private String route;
        private int source;
        private int destination;
        private char special;
        private Date dateOfTravel;

        public String getTrainNumber() {
            return trainNumber;
        }

        public void setTrainNumber(final String trainNumber) {
            this.trainNumber = trainNumber;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(final String route) {
            this.route = route;
        }

        public int getSource() {
            return source;
        }

        public void setSource(final int source) {
            this.source = source;
        }

        public int getDestination() {
            return destination;
        }

        public void setDestination(final int destination) {
            this.destination = destination;
        }

        public char getSpecial() {
            return special;
        }

        public void setSpecial(final char status) {
            this.special = special;
        }

        public void setDateOfTravel(final Date dateOfTravel){
            this.dateOfTravel= dateOfTravel;
        }
        public Date getDateOfTravel(){
            return dateOfTravel;
        }



        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            TrainDetailsVO other = (TrainDetailsVO) obj;

            if (trainNumber == null) {
                if (other.trainNumber != null) {
                    return false;
                }
            } else if (!trainNumber.equals(other.trainNumber)) {
                return false;
            }
            if (route == null) {
                if (other.route != null) {
                    return false;
                }
            } else if (!route.equals(other.route)) {
                return false;
            }
            if (special == ' ') {
                if (other.special != ' ') {
                    return false;
                }
            } else if (special != other.special) {
                return false;
            }
            if (destination != other.destination) {
                return false;
            }
            if (source != other.source) {
                return false;
            }

            return true;
        }

    }

    /* User defined Exception - DO NOT CHANGE */
    class TrainServiceException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public TrainServiceException(String message) {
            super(message);
        }

        public TrainServiceException(Throwable throwable) {
            super(throwable);
        }
    }
}
