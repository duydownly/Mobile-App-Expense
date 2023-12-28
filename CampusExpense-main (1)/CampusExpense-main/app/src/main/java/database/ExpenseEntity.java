package database;
import java.io.Serializable;


public class ExpenseEntity implements Serializable {
    public int id;
    public String expenseName;
    public String expenseDate;
    public int budgetId;
    public String budgetName;
    public String amount;

    public ExpenseEntity() {
    }
    @Override
    public String toString() {
        return  "Expense : "+expenseName + "\n" + amount +"$" + "\n" +expenseDate + "\n" + "Budget: "+budgetName + "\n"+ "Expense Code : "+ id;

    }
}
