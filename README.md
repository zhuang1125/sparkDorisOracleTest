#
## Oracle脚本
```agsl oracle11容器
registry.cn-hangzhou.aliyuncs.com/helowin/oracle_11g
zhuang1125/helowin_oracle_11g:v1
```
```agsl
create table HR.JOBS
(
    JOB_ID     VARCHAR2(10) not null
        constraint JOB_ID_PK
            primary key,
    JOB_TITLE  VARCHAR2(35) not null
        constraint JOB_TITLE_NN
            check ("JOB_TITLE" IS NOT NULL),
    MIN_SALARY NUMBER(6),
    MAX_SALARY NUMBER(6)
)
/

comment on table HR.JOBS is 'jobs table with job titles and salary ranges. Contains 19 rows.
References with employees and job_history table.'
/

comment on column HR.JOBS.JOB_ID is 'Primary key of jobs table.'
/

comment on column HR.JOBS.JOB_TITLE is 'A not null column that shows job title, e.g. AD_VP, FI_ACCOUNTANT'
/

comment on column HR.JOBS.MIN_SALARY is 'Minimum salary for a job title.'
/

comment on column HR.JOBS.MAX_SALARY is 'Maximum salary for a job title'
/


```


## Doris脚本
```agsl

CREATE TABLE `data_sync_test_simple` (
  `_id` varchar(32) NULL DEFAULT "",
  `id` varchar(32) NULL DEFAULT "",
  `user_name` varchar(32) NULL DEFAULT "",
  `member_list` varchar(32) NULL DEFAULT ""
) ENGINE=OLAP
DUPLICATE KEY(`_id`)
COMMENT 'OLAP'
DISTRIBUTED BY HASH(`_id`) BUCKETS 10
PROPERTIES (
"replication_allocation" = "tag.location.default: 1",
"in_memory" = "false",
"storage_format" = "V2",
"disable_auto_compaction" = "false"
);



CREATE TABLE `data_sync_test_oracledate` (
  `JOB_ID` varchar(10) NULL DEFAULT "",
  `JOB_TITLE` varchar(35) NULL DEFAULT "",
  `MIN_SALARY` int(11) NULL,
  `MAX_SALARY` int(11) NULL
) ENGINE=OLAP
UNIQUE KEY(`JOB_ID`, `JOB_TITLE`)
COMMENT 'OLAP'
DISTRIBUTED BY HASH(`JOB_ID`) BUCKETS 10
PROPERTIES (
"replication_allocation" = "tag.location.default: 1",
"in_memory" = "false",
"storage_format" = "V2",
"disable_auto_compaction" = "false"
);
```