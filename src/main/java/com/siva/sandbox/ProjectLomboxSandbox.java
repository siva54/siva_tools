package com.siva.sandbox;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
public class ProjectLomboxSandbox {
	public static void main(String[] args) {
		Demon demon = new Demon("Diablo", "Darkness");
		log.info("Demon is : " + demon.getName());
		log.info("Something");
	}

	@Data
	@NoArgsConstructor(staticName = "of")
	@RequiredArgsConstructor()
	static class Demon {
		private int id;
		@NonNull
		private String name;
		@NonNull
		private String type;
	}

}
