package server;

public class MyTimer implements Runnable
{
	private String place;
	Map m;
	public MyTimer(String s, Map m, int n)
	{
		place = s;
		this.m = m;
		this.m.color = n;
	}
	public void run()
	{
		try
		{
			m.setOn(place);
			Thread.currentThread().sleep(500L);
			m.setOff(place);
			Thread.currentThread().sleep(500L);
			m.setOn(place);
			Thread.currentThread().sleep(500L);
			m.setOff(place);
			Thread.currentThread().sleep(500L);
			m.setOn(place);
			Thread.currentThread().sleep(500L);
			m.setOff(place);

		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
}
