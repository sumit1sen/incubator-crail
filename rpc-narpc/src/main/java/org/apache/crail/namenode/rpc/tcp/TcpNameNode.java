/*
 * Crail: A Multi-tiered Distributed Direct Access File System
 *
 * Author: Patrick Stuedi <stu@zurich.ibm.com>
 *
 * Copyright (C) 2016, IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.crail.namenode.rpc.tcp;

import org.apache.crail.rpc.RpcBinding;
import org.apache.crail.rpc.RpcNameNodeService;
import org.apache.crail.rpc.RpcServer;

public class TcpNameNode extends TcpNameNodeClient implements RpcBinding {

	@Override
	public RpcServer launchServer(RpcNameNodeService service) {
		try {
			return new TcpNameNodeServer(service);
		} catch(Exception e){
			return null;
		}
	}

}