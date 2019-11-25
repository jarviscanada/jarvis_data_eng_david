CREATE TABLE IF NOT EXISTS PUBLIC.host_info (
	id SERIAL NOT NULL, 
	hostname VARCHAR(150) UNIQUE NOT NULL, 
	cpu_number SMALLINT, 
	cpu_architecture VARCHAR(50) NOT NULL, 
	cpu_model VARCHAR(150) NOT NULL, 
	cpu_mhz NUMERIC(7, 3), 
	L2_cache SMALLINT, 
	timestamp TIMESTAMP, 
				
	/* Define Primary Key & Foreign Key constraints here */
	PRIMARY KEY(id)
);
				
CREATE TABLE IF NOT EXISTS PUBLIC.host_usage (
	timestamp TIMESTAMP, 
	host_id INT, 
	memory_free INT, 
	cpu_idle NUMERIC, 
	cpu_kernel NUMERIC, 
	disk_io SMALLINT, 
	disk_available INT,
	
	/* Define Primary Key & Foreign Key constraints here */
	PRIMARY KEY(timestamp, host_id), 
	FOREIGN KEY(host_id) REFERENCES PUBLIC.host_info(id)
);

