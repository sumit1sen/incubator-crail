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

package com.ibm.crail.rpc;

import com.ibm.crail.metadata.BlockInfo;
import com.ibm.crail.metadata.FileInfo;

public interface RpcRenameFile extends RpcResponse {
	public FileInfo getSrcParent();
	public FileInfo getSrcFile();
	public FileInfo getDstParent();
	public FileInfo getDstFile();
	public BlockInfo getSrcBlock();
	public BlockInfo getDstBlock();
}
