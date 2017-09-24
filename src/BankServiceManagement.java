

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankServiceManagement {
    public static void main(String[] args) throws  Exception {
        BankServiceManagement b = new BankServiceManagement();
        Map<String, List<ParentAccountVO>> stringListMap = processBankDepositData("");
    }



    public static Map<String, List<ParentAccountVO>> processBankDepositData(
            String filePath) throws BankOrganizerException, FileNotFoundException {
        String s=null;
            Map<String,List<ParentAccountVO>> map=new HashMap<String,List<ParentAccountVO>>();
            String currLine=null;
            BankServiceManagement bao=new BankServiceManagement();
            SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-YYYY");
            FileReader file=new FileReader(filePath);
            BufferedReader br=new BufferedReader(file);
        try {
            List<ParentAccountVO> pst1=new ArrayList<>();
            List<ParentAccountVO> pst2=new ArrayList<>();
            List<ParentAccountVO> pst3=new ArrayList<>();

            while((currLine=br.readLine())!=null){
                ParentAccountVO po=new ParentAccountVO();
                    String[] values=currLine.split(",");
                boolean flag=false;
                    boolean v=validateData(values);
                    if(!v){
                        throw  new BankOrganizerException("validationfiled");
                    }
                    else{

                        List<LinkedDepositVO> lst=new ArrayList<>();
                        LinkedDepositVO vo=new LinkedDepositVO();
                        int depoAmount=Integer.parseInt(values[4]);
                        Date startDate=formatter.parse(values[5]);
                        Date endDate=formatter.parse(values[6]);
                        vo.setDepositStartDate(startDate);
                        vo.setDepositMaturityDate(endDate);
                        vo.setDepositAmount(Integer.parseInt(values[4]));
                        vo.setMaturityAmount(bao.calculateMaturityAmount(startDate,endDate,depoAmount));

                        if (values[2].equals("WM")) {

                            for(ParentAccountVO parentAccountVO:pst1){

                                if(parentAccountVO.getParentAccNo()==Integer.parseInt(values[0])){
                                    flag=true;
                                    List<LinkedDepositVO> linkedDepositVOs = parentAccountVO.getLinkedDeposits();
                                    linkedDepositVOs.add(vo);
                                }

                            }

                        }

                        if (values[2].equals("SAV")) {

                            for(ParentAccountVO parentAccountVO:pst2){

                                if(parentAccountVO.getParentAccNo()==Integer.parseInt(values[0])){
                                    flag=true;
                                    List<LinkedDepositVO> linkedDepositVOs = parentAccountVO.getLinkedDeposits();
                                    linkedDepositVOs.add(vo);
                                }

                            }

                        }
                        if (values[2].equals("NRI")) {

                            for(ParentAccountVO parentAccountVO:pst3){

                                if(parentAccountVO.getParentAccNo()==Integer.parseInt(values[0])){
                                    flag=true;
                                    List<LinkedDepositVO> linkedDepositVOs = parentAccountVO.getLinkedDeposits();
                                    linkedDepositVOs.add(vo);
                                }

                            }

                        }
                        if(!flag){
                            lst.add(vo);
                            po.setName(values[1]);
                            po.setAccType(values[2]);
                            po.setParentAccNo(Integer.parseInt(values[0]));
                            po.setLinkedDeposits(lst);
                            if (values[2].equals("WM")) {
                                pst1.add(po);
                            }
                            if (values[2].equals("SAV")) {
                                pst2.add(po);
                            }

                            if (values[2].equals("NRI")) {
                                pst3.add(po);
                            }



                        }

                    }

                map.put("WM", pst1);

                map.put("SAV", pst2);

                map.put("NRI", pst3);

            }

        } catch (IOException e) {
           throw new BankOrganizerException("Not valid");
        } catch (ParseException e) {
            throw new BankOrganizerException("Pasrinval");
        }
        return map;
    }

    private float calculateMaturityAmount(Date date1, Date date2,int depositamount){
        float maturity_amount=0.00f;
        long days=((date2.getTime()-date1.getTime())/(1000*24*60*60));
        if(days >= 0 && days <= 200){
            maturity_amount=(float)(depositamount +(depositamount*6.75/100));
        }else if(days >= 201 && days <= 400){
            maturity_amount=(float)(depositamount +(depositamount*7.5/100));
        }else if(days >= 401 && days <= 600){
            maturity_amount=(float)(depositamount +(depositamount*8.75/100));
        }else {
            maturity_amount=(float)(depositamount +(depositamount*10/100));

        }

    return maturity_amount;
    }

    public static boolean validateData(String[] str) {
        //write your code here
        SimpleDateFormat fort=new SimpleDateFormat("dd-MM-yyyy");
        boolean value=true;
        if (str[0].isEmpty() || str[1].isEmpty() || str[2].isEmpty() || str[3].isEmpty() || str[4].isEmpty() || str[5].isEmpty() || str[6].isEmpty()) {
            value = false;
        }
        if(str[0].charAt(0)==0){
            value=false;
        }
        else
            for(char c:str[0].toCharArray())
            if(!Character.isDigit(c)){
            value=false;
            break;
            }
            if(!(str[2].equals("WM")) || (str[2].equals("SAV")) || str[2].equals("NRI")){
                value=false;
            }
            if(!(str[3].equals("FD"))  ||(str[3].equals("RD")) || (str[3].equals("MUT"))){
                value=false;
            }

        try {
            Date d=fort.parse(str[5]);
                try {
                    if(!str[5].equals(fort.format(d)))
                    throw new BankOrganizerException("notvalid");
                } catch (BankOrganizerException e) {
                    e.printStackTrace();
                }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date d1=fort.parse(str[6]);
            try {
                if(!str[6].equals(fort.format(d1)))
                    throw new BankOrganizerException("notvalid");
            } catch (BankOrganizerException e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return  value;
    }


}


class ParentAccountVO {

    private int parentAccNo;
    private String name;
    private String AccType;
    //private LinkedDepositVO linkedDeposit;
    private List<LinkedDepositVO> linkedDeposits;

    public int getParentAccNo() {
        return parentAccNo;
    }

    public void setParentAccNo(int parentAccNo) {
        this.parentAccNo = parentAccNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccType() {
        return AccType;
    }

    public void setAccType(String accType) {
        AccType = accType;
    }

    public List<LinkedDepositVO> getLinkedDeposits() {
        return linkedDeposits;
    }

    public void setLinkedDeposits(List<LinkedDepositVO> linkedDeposits) {
        this.linkedDeposits = linkedDeposits;
    }

    public boolean equals(Object object) {
        boolean isEqual = false;
        ParentAccountVO otherAccount = (ParentAccountVO) object;
        if ((this.parentAccNo == otherAccount.parentAccNo)
                && (this.AccType.equals(otherAccount.getAccType()) && (this.linkedDeposits
                .equals(otherAccount.getLinkedDeposits())))) {
            isEqual = true;
        }
        return isEqual;
    }



    @Override
    public String toString() {
        return "ParentAccountVO [parentAccNo=" + parentAccNo + ", name=" + name
                + ", AccType=" + AccType + ", linkedDeposits=" + linkedDeposits
                + "]";

        //	return parentAccNo  + "  , " +  name  + " ," + AccType + " ," +  linkedDeposits;

    }

}

class LinkedDepositVO {

    private String linkedDepositNo;
    private int depositAmount;
    private Date depositStartDate;
    private Date depositMaturityDate;
    private float maturityAmount;

    public String getLinkedDepositNo() {
        return linkedDepositNo;
    }

    public void setLinkedDepositNo(String linkedDepositNo) {
        this.linkedDepositNo = linkedDepositNo;
    }

    public int getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(int depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Date getDepositStartDate() {
        return depositStartDate;
    }

    public void setDepositStartDate(Date depositStartDate) {
        this.depositStartDate = depositStartDate;
    }

    public Date getDepositMaturityDate() {
        return depositMaturityDate;
    }

    public void setDepositMaturityDate(Date depositMaturityDate) {
        this.depositMaturityDate = depositMaturityDate;
    }

    public float getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(float maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public boolean equals(Object object) {
        boolean isEquals = false;
        LinkedDepositVO depositVO = (LinkedDepositVO) object;
        if (this.linkedDepositNo.equals(depositVO.getLinkedDepositNo())
                && (this.depositAmount == depositVO.getDepositAmount())
                && (this.depositStartDate.equals(depositVO
                .getDepositStartDate()))
                && (this.maturityAmount == depositVO.getMaturityAmount())) {
            isEquals = true;
        }
        return isEquals;
    }

    @Override
    public String toString() {


        return "LinkedDepositVO [linkedDepositNo=" + linkedDepositNo
                + ", depositAmount=" + depositAmount + ", depositStartDate="
                + depositStartDate + ", depositMaturityDate="
                + depositMaturityDate + ", maturityAmount=" + maturityAmount
                + "]";

        //	return linkedDepositNo  + "  , " +  depositAmount  + " ," + depositStartDate + " ," +  depositMaturityDate + "," + maturityAmount;
    }

}

class BankOrganizerException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BankOrganizerException(String message) {
        super(message);
    }

    public BankOrganizerException(Throwable throwable) {
        super(throwable);
    }

    public BankOrganizerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

/************************************************************/
/*
 * DO NOT CHANGE THE BELOW CLASS. THIS IS FOR VERIFYING THE CLASS NAME AND
 * METHOD SIGNATURE USING REFLECTION APIs
 */
/************************************************************/
class Validator {

    private static final Logger LOG = Logger.getLogger("Validator");

    public Validator(String filePath, String className, String methodWithExcptn) {
        validateStructure(filePath, className, methodWithExcptn);
    }

    protected final void validateStructure(String filePath, String className,
                                           String methodWithExcptn) {

        if (validateClassName(className)) {
            validateMethodSignature(methodWithExcptn, className);
        }

    }

    protected final boolean validateClassName(String className) {

        boolean iscorrect = false;
        try {
            Class.forName(className);
            iscorrect = true;
            LOG.info("Class Name is correct");

        } catch (ClassNotFoundException e) {
            LOG.log(Level.SEVERE, "You have changed either the "
                    + "class name/package. Use the default package "
                    + "and class name as provided in the skeleton");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "There is an error in validating the "
                    + "Class Name. Please manually verify that the "
                    + "Class name is same as skeleton before uploading");
        }
        return iscorrect;

    }

    protected final void validateMethodSignature(String methodWithExcptn,
                                                 String className) {
        Class cls;
        try {

            String[] actualmethods = methodWithExcptn.split(",");
            boolean errorFlag = false;
            String[] methodSignature;
            String methodName = null;
            String returnType = null;

            for (String singleMethod : actualmethods) {
                boolean foundMethod = false;
                methodSignature = singleMethod.split(":");

                methodName = methodSignature[0];
                returnType = methodSignature[1];
                cls = Class.forName(className);
                Method[] methods = cls.getMethods();
                for (Method findMethod : methods) {
                    if (methodName.equals(findMethod.getName())) {
                        foundMethod = true;
                        if ((findMethod.getExceptionTypes().length != 1)) {
                            LOG.log(Level.SEVERE, "You have added/removed "
                                    + "Exception from '" + methodName
                                    + "' method. "
                                    + "Please stick to the skeleton provided");
                        }
                        if (!(findMethod.getReturnType().getName()
                                .equals(returnType))) {
                            errorFlag = true;
                            LOG.log(Level.SEVERE, " You have changed the "
                                    + "return type in '" + methodName
                                    + "' method. Please stick to the "
                                    + "skeleton provided");

                        }

                    }
                }
                if (!foundMethod) {
                    errorFlag = true;
                    LOG.log(Level.SEVERE,
                            " Unable to find the given public method "
                                    + methodName + ". Do not change the "
                                    + "given public method name. "
                                    + "Verify it with the skeleton");
                }

            }
            if (!errorFlag) {
                LOG.info("Method signature is valid");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE,
                    " There is an error in validating the "
                            + "method structure. Please manually verify that the "
                            + "Method signature is same as the skeleton before uploading");
        }
    }

}
