/*
 * Copyright 2016 ikidou
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
 */

package com.vm.handler;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.vm.Resp;
import com.vm.db.DB;
import com.vm.entity.Blog;
import com.vm.util.PushExample;
import com.vm.util.StringUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;

import static com.j256.ormlite.dao.DaoManager.createDao;

public enum PayHandler implements Route {
	//Get pay result
    GET {
        @Override
        public Object handle(Request request, Response response) throws Exception {
        	String id = getId(request);
            Object result = new Object();
            System.out.println("Get pay result for id: " + id);
            Resp resp = null;
            
            if(id != null) {
            	result = ContextHolder.getInstantce().getPaidResult(id);
            }
            
            if(ContextHolder.paid) {
            	resp = Resp.create(200, ContextHolder.PAID);
            } else {
            	resp = Resp.create(200, ContextHolder.NOT_PAID);
            }

            resp.count = 1;
            resp.page = 1;

            return resp;
        }
    },

    PUT {
        @Override
        public Object handle(Request request, Response response) throws Exception {
        	String id = this.getId(request);
        	String paidStatus = this.getPaidStatus(request);
        	
        	System.out.println("setPaid for id: " +id + ", status: " + paidStatus);
        	
        	if(id != null) {
        		ContextHolder.getInstantce().setPaid(id);
        	}
        	
        	if(ContextHolder.PAID.equals(paidStatus)) {
            	ContextHolder.paid = true;
        	} else {
            	ContextHolder.paid = false;        		
        	}
        	
        	return Resp.create(200, id);
        }
    },
    
    DELETE {
        @Override
        public Object handle(Request request, Response response) throws Exception {
                return Resp.create(200, "OK");
        }
    },

    HEAD {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            return Resp.create(200, "OK");
        }
    };
	
	public String getId(Request request) {
		String id = request.params("id");
		String idInQueryString = request.queryParams("id");

		if (id == null && idInQueryString == null) {
			return null;
		} else if (id == null) {
			id = idInQueryString;
		}

		return id;
	}

	public String getPaidStatus(Request request) {
		String status = request.params("status");
		String statusInQueryString = request.queryParams("status");

		if (status == null && statusInQueryString == null) {
			return null;
		} else if (status == null) {
			status = statusInQueryString;
		}

		return status;
	}
	 
    public Dao<Blog, Long> getDao() throws SQLException {
        return createDao(DB.getConnectionSource(), Blog.class);
    }
}
