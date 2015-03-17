package com.mnubo.platform.android.sdk.internal.connect.query;

import com.mnubo.platform.android.sdk.models.common.IdType;
import com.mnubo.platform.android.sdk.models.common.ValueType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.mnubo.platform.android.sdk.BuildConstants.PATH;
import static com.mnubo.platform.android.sdk.Constants.DETAILS;
import static com.mnubo.platform.android.sdk.Constants.FROM;
import static com.mnubo.platform.android.sdk.Constants.ID_TYPE;
import static com.mnubo.platform.android.sdk.Constants.LIMIT;
import static com.mnubo.platform.android.sdk.Constants.TO;
import static com.mnubo.platform.android.sdk.Constants.UPDATE_IF_EXISTS;
import static com.mnubo.platform.android.sdk.Constants.VALUE_TYPE;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

public class PlatformQuery {

    private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    private List<String> uriVariables = new ArrayList<>();

    private String uri = "";

    private String platformBaseUrl;

    private PlatformPath platformPath;

    private Object body;

    public PlatformQuery(String platformBaseUrl, PlatformPath platformPath) {
        this(platformBaseUrl, PATH, platformPath);
    }

    public PlatformQuery(String platformBaseUrl, String rootPath, PlatformPath platformPath) {
        Validate.notBlank(platformBaseUrl, "Platform base url cannot be null or empty.");
        Validate.notBlank(rootPath, "Platform base url cannot be null or empty.");
        Validate.notNull(platformPath, "Platform path cannot be null.");

        this.platformPath = platformPath;
        this.platformBaseUrl = platformBaseUrl + rootPath;
    }

    public void setUri(String uri, String... uriVariables) {
        this.uri = uri;
        this.setUriVariables(uriVariables);
    }

    void setUriVariables(String... uriVariables) {
        for (String uriVariable : uriVariables) {
            if (StringUtils.isNotBlank(uriVariable)) {
                this.uriVariables.add(uriVariable);
            }
        }
    }

    public PlatformQuery value(ValueType valueType) {
        if (valueType == null) {
            return this;
        }
        return queryParam(VALUE_TYPE, valueType.toString());
    }

    public PlatformQuery limit(int limit) {
        if (limit <= 0) {
            return this;
        }
        return queryParam(LIMIT, limit);
    }

    public PlatformQuery timeRange(String from, String to) {
        queryParam(FROM, from);
        queryParam(TO, to);
        return this;
    }

    public PlatformQuery details(boolean details) {
        return queryParam(DETAILS, details);
    }

    public PlatformQuery idType(IdType idType) {
        return queryParam(ID_TYPE, idType.toString());
    }

    public PlatformQuery updateIfExists(boolean updateIfExists) {
        queryParams.set(UPDATE_IF_EXISTS, String.valueOf(updateIfExists));
        return this;
    }

    public PlatformQuery queryParam(String key, Object value) {
        if (StringUtils.isNotBlank(key) && value != null) {
            this.queryParams.add(key, value.toString());
        }

        return this;
    }

    public PlatformQuery queryParams(String key, List<String> values) {
        if (StringUtils.isNotBlank(key) && !CollectionUtils.isEmpty(values)) {
            this.queryParams.put(key, values);
        }

        return this;
    }

    public PlatformQuery setBody(Object body) {
        this.body = body;
        return this;
    }


    public String buildUrl() {
        return URIBuilder.fromUri(this.build()).queryParams(this.queryParams).build().toString();
    }

    String build() {

        String finalUrl = String.format("%s/%s%s", this.platformBaseUrl, this.platformPath, this.uri);

        UriComponentsBuilder builder = fromHttpUrl(finalUrl);

        return builder.buildAndExpand(this.getUriVariables()).toString();
    }

    Object[] getUriVariables() {
        return uriVariables.toArray();
    }

    public Object getBody() {
        return body;
    }
}
