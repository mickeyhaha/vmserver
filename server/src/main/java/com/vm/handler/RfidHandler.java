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

public enum RfidHandler implements Route {
    GET {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            Object result = new Object();
            System.out.println("======================here i am");
            Resp resp = Resp.create(200, "OK", result);

            resp.count = 1;
            resp.page = 1;

            return resp;
        }
    },

    POST {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            Gson gson = new Gson();
            Blog blog = gson.fromJson(request.body(), Blog.class);

            String field = null;
            if (StringUtils.isEmpty(blog.author)) {
                field = "author";
            } else if (StringUtils.isEmpty(blog.content)) {
                field = "content";
            } else if (StringUtils.isEmpty(blog.title)) {
                field = "title";
            }
            if (field != null) {
                return Resp.create(400, " `" + field + "` is empty!");
            } else {
                getDao().create(blog);
                return Resp.create(200, "OK", blog);
            }
        }
    },

    PUT {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            long id = getId(request);
            if (id >= 0) {
                Blog blog = getDao().queryForId(id);
                if (blog == null) {
                    return Resp.create(400, "No Such Element:" + id);
                } else {
                    Gson gson = new Gson();
                    Blog requestBody = gson.fromJson(request.body(), Blog.class);
                    if (requestBody.content == null && requestBody.title == null && requestBody.author == null) {
                        return Resp.create(400, "Can't find any field of Blog", gson.fromJson(request.body(), Map.class));
                    } else {
                        if (!StringUtils.isEmpty(requestBody.author)) {
                            blog.author = requestBody.author;
                        }
                        if (!StringUtils.isEmpty(requestBody.title)) {
                            blog.title = requestBody.title;
                        }
                        if (!StringUtils.isEmpty(requestBody.content)) {
                            blog.content = requestBody.content;
                        }
                        getDao().update(blog);
                        return Resp.create(200, "OK", blog);
                    }
                }

            }

            return Resp.create(400, "Miss `id` attribute");
        }
    },

    DELETE {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            long id = getId(request);
            if (id >= 0) {
                getDao().deleteById(id);
                return Resp.create(200, "OK");
            }
            return Resp.create(400, "Miss `id` attribute");
        }
    },

    HEAD {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            return Resp.create(200, "OK");
        }
    };

    public long getId(Request request) {
        String id = request.params("id");
        String idInQueryString = request.queryParams("id");

        if (id == null && idInQueryString == null) {
            return -1;
        } else if (id == null) {
            id = idInQueryString;
        }
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("bad id:`" + id + "`");
        }
    }

    public Dao<Blog, Long> getDao() throws SQLException {
        return createDao(DB.getConnectionSource(), Blog.class);
    }
}
