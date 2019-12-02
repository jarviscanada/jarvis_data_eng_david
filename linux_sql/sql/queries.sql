-- delete view if already exist to update upon running script
DROP VIEW IF EXISTS cpu_num_by_mem_size;
DROP VIEW IF EXISTS avg_mem_usage;
DROP VIEW IF EXISTS avg_mem_temp;
DROP TABLE IF EXISTS test_t;
DROP VIEW IF EXISTS temp_fin;
DROP TABLE IF EXISTS node_fail CASCADE;


---------- START first question ----------
-- Group host by hardware information
CREATE VIEW cpu_num_by_mem_size AS 
SELECT 
  FIRST_VALUE(cpu_number) OVER (PARTITION BY id  ORDER BY total_mem DESC) AS cpu_number, 
  id as host_id, 
  total_mem 
FROM 
  PUBLIC.host_info
ORDER BY 
  cpu_number;
---------- END first question ----------


---------- START second question ----------
-- Temporary table for 2nd sql query
CREATE TABLE test_t AS
SELECT 
  hi.id,
  hi.hostname,
  hi.total_mem,
  CAST(hu.memory_free*1024 AS INT) AS memory_free,
  hu.timestamp as timestamp
FROM 
  PUBLIC.host_info hi
  RIGHT OUTER JOIN PUBLIC.host_usage hu ON hi.id = hu.host_id;

-- add updated rounded timestamp to table
ALTER TABLE test_t
ADD COLUMN round_tstamp timestamp;

--Start of section to update table to obtain percent memory used --
UPDATE test_t
SET round_tstamp = date_trunc('hour', timestamp) + INTERVAL '5 min' * ROUND(date_part('minute', timestamp) / 5.0);

ALTER TABLE test_t
ADD COLUMN percent_mem_numeric NUMERIC;

UPDATE test_t
SET percent_mem_numeric = CAST(memory_free AS NUMERIC)/total_mem*100;

ALTER TABLE test_t
ADD COLUMN percent_mem_used INT;

UPDATE test_t
SET percent_mem_used = CAST(100 - percent_mem_numeric AS INT);
--End of section to update table to obtain percent memory used --


--GROUP BY to collapse timestamp
CREATE VIEW avg_mem_temp AS
SELECT
  id,
  hostname,
  total_mem,
  AVG(percent_mem_used) OVER (PARTITION BY round_tstamp ORDER BY round_tstamp) AS percent_mem_used,
  round_tstamp
FROM
  test_t g;

-- Solution to 2nd problem
CREATE VIEW avg_mem_usage AS
SELECT  
  id AS host_id,
  hostname AS host_name,
  total_mem AS total_memory,
  CAST(AVG(percent_mem_used) AS SMALLINT) AS used_memory_percentage
FROM
  avg_mem_temp
GROUP BY
  id,hostname,total_mem;
---------- END second question ----------

---------- START final question ----------

-- Temp view to remove seconds
CREATE VIEW temp_fin AS
SELECT
  host_id,
  date_trunc('minute',timestamp) AS timestamp
FROM
  PUBLIC.host_usage
ORDER BY timestamp;


-- Add timestamp of next and next next to table
CREATE TABLE node_fail AS
SELECT
  host_id,
  date_trunc('minute',timestamp) AS timestamp,
  LEAD(timestamp,1) OVER (PARTITION BY host_id ORDER BY timestamp) as next_tstamp
FROM
  temp_fin;

-- FALSE: FAILED  -- TRUE: ok
ALTER TABLE node_fail
ADD COLUMN is_failed BOOLEAN DEFAULT FALSE;

UPDATE node_fail
SET is_failed=
		CASE
			WHEN next_tstamp IS NULL then TRUE
			WHEN EXTRACT(MINUTE FROM next_tstamp-timestamp)<=3 then TRUE
			ELSE FALSE
		END;


-- Start of table block to remove bool col, include failed times, and remove col next timestamp --
DELETE FROM node_fail
WHERE 
  is_failed = TRUE;
 
ALTER TABLE node_fail
DROP COLUMN is_failed;

ALTER TABLE node_fail
ADD COLUMN failed_times INT;

UPDATE node_fail
SET failed_times = EXTRACT(EPOCH FROM next_tstamp-timestamp)/60;

-- solution of final problem
ALTER TABLE node_fail
DROP COLUMN next_tstamp;
-- End of table block --
---------- END final question ----------


SELECT * FROM cpu_num_by_mem_size; -- output sol to 1st q
SELECT * FROM avg_mem_usage; -- output sol to 2nd q
SELECT * FROM node_fail; -- output sol to 3rd q



