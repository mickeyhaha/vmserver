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

package com.github.ikidou.handler;

import com.github.ikidou.Resp;
import com.github.ikidou.db.DB;
import com.github.ikidou.entity.Blog;
import com.github.ikidou.util.PushExample;
import com.github.ikidou.util.StringUtils;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

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

public enum VmCallbackHandler implements Route {
	//Get rfids
    GET {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            Object result = new Object();
            String rfids = this.getRfids(request);
            System.out.println("rfids: " + rfids);
            Resp resp = Resp.create(200, "OK", result);

            resp.count = 1;
            resp.page = 1;
            
            ContextHolder.paid = false;

            return resp;
        }
    };

    public String getRfids(Request request) {
        String rfids = request.params("rfids");
        String rfidsInQueryString = request.queryParams("rfids");

        if (rfids == null && rfidsInQueryString == null) {
            return null;
        } else if (rfids == null) {
            rfids = rfidsInQueryString;
        }
        return rfids;
    }
}
