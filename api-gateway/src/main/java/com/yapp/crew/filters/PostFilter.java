package com.yapp.crew.filters;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import java.io.InputStream;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j(topic = "Zuul Post Filter")
@Component
public class PostFilter extends ZuulFilter {

	@Override
	public String filterType() {
		return POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return SEND_RESPONSE_FILTER_ORDER - 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		try (final InputStream responseDataStream = ctx.getResponseDataStream()) {

			if(responseDataStream == null) {
				log.info("Response Body: null");
				return null;
			}

			String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));
			log.info("Response Body: {}", responseData);

			ctx.setResponseBody(responseData);
			ctx.getResponse().setContentType("application/json;charset=UTF-8");
		}
		catch (Exception e) {
			throw new ZuulException(e, INTERNAL_SERVER_ERROR.value(), e.getMessage());
		}

		return null;
	}
}
