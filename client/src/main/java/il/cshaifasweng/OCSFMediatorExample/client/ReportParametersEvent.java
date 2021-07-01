package il.cshaifasweng.OCSFMediatorExample.client;

public class ReportParametersEvent{

	private final String reportTitle;
	private String ReportName;
	private String date;
	private String privilege;

	public ReportParametersEvent(String Name, String reportTitle, String privilege) {
		this.reportTitle = reportTitle;
		this.ReportName = Name;
		this.privilege = privilege;
	}

	//public ReportParametersEvent(String Name, String date, String privilege) {
	//	this.ReportName = Name;
	//	this.date = date;
	//	this.privilege = privilege;
	//}
	
	public String getReportName() {
		return ReportName;
	}

	public String getDate() {
		return date;
	}

	public void setReportName(String Name) {
		this.ReportName = Name;
	}

	public void setReportDate(String date) {
		this.date = date;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

    public String getReportTitle() {
		return reportTitle;
    }
}