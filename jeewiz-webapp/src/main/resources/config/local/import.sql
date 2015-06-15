/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

INSERT INTO PUBLIC.ROLE(ID, RL_NM, NM, DSCR) values (10, 'ROLE_ADMIN', 'Administrator', 'Allows the user to administer other users');
INSERT INTO PUBLIC.ROLE(ID, RL_NM, NM, DSCR) values (20, 'ROLE_USER', 'User', 'Basic access to recipe book resources');

INSERT INTO PUBLIC.ACCT (ID, DT_CR, DT_LM, VER, ENBLD, CRD_EXP, LCKD, EXP, PWD, USR_NM, EMAIL ) VALUES ('4028808544e2f9700144e2f98e070000', NOW(), NOW(),0,'Y','N','N','N','ac9689e2272427085e35b9d3e3e8bed88cb3434828b43b86fc0596cad4c6e270','admin', 'admin@jeewiz.com');

INSERT INTO PUBLIC.ACCT_ROLE(ACCT_ID, ROLE_ID) VALUES ('4028808544e2f9700144e2f98e070000', 10);
INSERT INTO PUBLIC.ACCT_ROLE(ACCT_ID, ROLE_ID) VALUES ('4028808544e2f9700144e2f98e070000', 20);