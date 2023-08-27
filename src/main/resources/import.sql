INSERT INTO `Organisation`(`id`,`name`,`abn`) values (1, 'Australian Agent for International Students', 11111111111);
INSERT INTO `Organisation`(`id`,`name`,`abn`) values (2, 'CRP AUSTRALIA PTY LTD',22222222222);
INSERT INTO `Organisation`(`id`,`name`,`abn`) values (3, 'THE AUSTRALIAN',33333333333);

INSERT INTO `Contact`(`id`,`first_name`, `last_name`, `created_at`, `organisation_id`) values (1, 'Sophie', 'Klein', '2022-12-31', 1);
INSERT INTO `Contact`(`id`,`first_name`, `last_name`, `created_at`, `organisation_id`) values (3, 'Alma', 'Armstrong','2022-12-31', 1);
INSERT INTO `Contact`(`id`,`first_name`, `last_name`, `created_at`, `organisation_id`) values (4, 'Alexander', 'Morris','2022-12-31', 2);
INSERT INTO `Contact`(`id`,`first_name`, `last_name`, `created_at`, `organisation_id`) values (5, 'Wm', 'Brady', '2022-12-31',2);
INSERT INTO `Contact`(`id`,`first_name`, `last_name`, `created_at`, `organisation_id`) values (6, 'Rudy', 'Bush', '2022-12-31',2);
INSERT INTO `Contact`(`id`,`first_name`, `last_name`, `created_at`, `organisation_id`) values (7, 'Andres', 'Mcguire', '2022-12-31',2);
INSERT INTO `Contact`(`id`,`first_name`, `last_name`, `created_at`, `organisation_id`) values (8, 'Nick', 'Lane', '2022-12-31',3);
INSERT INTO `Contact`(`id`,`first_name`, `last_name`, `created_at`, `organisation_id`) values (9, 'Alesa', 'Peter','2022-12-31', 1);