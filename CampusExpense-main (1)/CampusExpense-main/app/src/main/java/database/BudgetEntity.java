package database;

public class BudgetEntity {
        public int id; // Assuming there's an ID field in your Budget table
        public   String budgetName;
        public double limitedBudget;
        public double expenseBudget;

        // Constructors, getters, and setters for the fields

        // Assuming you have a method to get the ID
        public BudgetEntity() {
        }

        @Override
        public String toString() {
                return  "Budget: "+ budgetName + "\n" + limitedBudget+"$" + "\n" +"BudgetCode"+ id ;

        }
        public double getExpenseBudget() {
                return   expenseBudget;
        }
}

