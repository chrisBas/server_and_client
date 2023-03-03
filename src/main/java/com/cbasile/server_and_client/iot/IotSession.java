package com.cbasile.server_and_client.iot;

public class IotSession {
	
	private IotState state;
	private String name;
	private Long id;
	private boolean available;
	private String linkedId;

	public IotState getState() {
		return state;
	}

	public void setState(IotState state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getLinkedId() {
		return linkedId;
	}

	public void setLinkedId(String linkedId) {
		this.linkedId = linkedId;
	}

}
