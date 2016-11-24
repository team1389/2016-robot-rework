package com.team1389.hardware.configuration;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class PIDController implements LiveWindowSendable {
	public PIDController(PIDConstants p,double setpoint){
		this.current=p;
		this.setpoint=setpoint;
	}
	  PIDConstants current;
	  double setpoint;
	  @Override
	  public String getSmartDashboardType() {
	    return "PIDController";
	  }
	  private final ITableListener listener = new ITableListener() {
	    @Override
	    public void valueChanged(ITable table, String key, Object value, boolean isNew) {
	      if (key.equals("p") || key.equals("i") || key.equals("d")) {
	        if (current.p != table.getNumber("p", 0.0) || current.i != table.getNumber("i", 0.0)
	            || current.d != table.getNumber("d", 0.0))
	          setPID(table.getNumber("p", 0.0), table.getNumber("i", 0.0), table.getNumber("d", 0.0));
	      } else if (key.equals("setpoint")) {
	        if (setpoint != ((Double) value).doubleValue())
	          setpoint=((Double) value).doubleValue();
	      }
	    }
	  };
	  public void setPID(double p,double i,double d){
		  current=new PIDConstants(p,i,d);
	  }
	  private ITable table;
	
	  @Override
	  public void initTable(ITable table) {
	    if (this.table != null)
	      this.table.removeTableListener(listener);
	    this.table = table;
	    if (table != null) {
	      table.putNumber("p", current.p);
	      table.putNumber("i", current.i);
	      table.putNumber("d", current.d);
	      table.putNumber("setpoint", setpoint);
	      table.addTableListener(listener, false);
	    }
	  }
	  public PIDConstants getPID(){
		  return current;
	  }
	  public double getSetpoint(){
		  return setpoint;
	  }
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public ITable getTable() {
	    return table;
	  }
	
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void updateTable() {}
	
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void startLiveWindowMode() {
	  }
	
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void stopLiveWindowMode() {}
}

