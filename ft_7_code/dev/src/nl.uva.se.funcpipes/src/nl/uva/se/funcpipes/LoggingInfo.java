package nl.uva.se.funcpipes;

public class LoggingInfo {
	String job;
	String username;
	String password;
	Integer yearsInJob;
	Integer projects;


 public LoggingInfo(String job,String username,String password, Integer years, Integer projects){
	this.job = job;
	this.username = username;
	this.password = password;
	this.yearsInJob = years;
	this.projects = projects;
 }
 
 public String getJob() {
	 return this.job;
 }
 
 public String getUser() {
	 return this.username;
 }
 
 public String getPass() {
	 return this.password;
 }
 
 public Integer getYearsInJob() {
	 return this.yearsInJob;
 }
 
 public Integer getProjects() {
	 return this.projects;
 }
 
 public String toString() {
     return "Job: " + getJob() + ", Username: " + getUser() + ", Password: " + getPass()
     + "Years in Job: " + getYearsInJob()+ ", Projects " + getProjects() + "\n";
 }


}