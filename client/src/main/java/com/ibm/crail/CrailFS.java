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

package com.ibm.crail;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import com.ibm.crail.conf.CrailConfiguration;
import com.ibm.crail.conf.CrailConstants;
import com.ibm.crail.core.CoreFileSystem;
import com.ibm.crail.utils.CrailUtils;

public abstract class CrailFS {
	private static final Logger LOG = CrailUtils.getLogger();
	private static AtomicLong referenceCounter = new AtomicLong(0);
	private static CrailFS instance = null;
	
	protected abstract Future<CrailFile> create(String path, boolean isDir, int storageAffinity, int locationAffinity) throws Exception;
	public abstract Future<CrailFile> lookupFile(String path, boolean writeable) throws Exception;
	public abstract Future<CrailFile> rename(String src, String dst) throws Exception;
	public abstract Future<CrailFile> delete(String path, boolean recursive) throws Exception;
	public abstract Iterator<String> listEntries(String name) throws Exception;
	public abstract CrailBlockLocation[] getBlockLocations(String path, long start, long len) throws Exception;
	public abstract void dumpNameNode() throws Exception;
	public abstract ByteBuffer allocateBuffer() throws IOException;
	public abstract void freeBuffer(ByteBuffer buffer) throws IOException;
	public abstract int getHostHash();
	public abstract void printStatistics(String message);
	public abstract void resetStatistics();
	protected abstract void closeFileSystem() throws Exception;
	
	public static CrailFS newInstance(CrailConfiguration conf) throws Exception {
		synchronized(referenceCounter){
			boolean isSingleton = conf.getBoolean(CrailConstants.SINGLETON_KEY, false);
			if (isSingleton) {
				referenceCounter.incrementAndGet();
				if (instance == null) {
					LOG.info("creating singleton crail file system");
					instance = new CoreFileSystem(conf);
					return instance;
				} else {
					LOG.info("returning singleton crail file system");
					return instance;
				}
			} else {
				LOG.info("creating non-singleton crail file system");				
				return new CoreFileSystem(conf);
			}
		}
	}
	
	public CrailMultiStream getMultiStream(Iterator<String> paths, int outstanding) throws Exception{
		return new CrailMultiStream(this, paths, outstanding);
	}	
	
	public Future<CrailFile> createFile(String path, int locationAffinity, int storageAffinity) throws Exception {
		return create(path, false, locationAffinity, storageAffinity);
	}	
	
	public Future<CrailFile> createDir(String path) throws Exception {
		return create(path, true, 0, 0);
	}

	public void close() throws Exception {
		synchronized(referenceCounter){
			if (CrailConstants.SINGLETON){
				long counter = referenceCounter.decrementAndGet();
				if (counter == 0){
					LOG.info("Closing CrailFS singleton");
					try {
						closeFileSystem();
					} catch (Exception e){
						throw new IOException(e);
					}
				}
			} else {
				LOG.info("Closing CrailFS non-singleton");
				try {
					closeFileSystem();
				} catch (Exception e){
					throw new IOException(e);
				}				
			}
			
		}
	}
}
