package com.okx.open.api.service.trade.impl;

import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.okx.open.api.bean.trade.param.AmendOrder;
import com.okx.open.api.bean.trade.param.CancelAlgoOrder;
import com.okx.open.api.bean.trade.param.CancelOrder;
import com.okx.open.api.bean.trade.param.ClosePositions;
import com.okx.open.api.bean.trade.param.PlaceOrder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 
 * @author spb512
 * @date 2022年7月24日
 */
public interface TradeApi {

	/**
	 * 下单 Place Order
	 * 
	 * @param jsonObject
	 * @return
	 */
	@POST("/api/v5/trade/order")
	Call<JSONObject> placeOrder(@Body JSONObject jsonObject);

	/**
	 * 批量下单 Place Multiple Orders
	 * 
	 * @param placeOrders
	 * @return
	 */
	@POST("/api/v5/trade/batch-orders")
	Call<JSONObject> placeMultipleOrders(@Body List<PlaceOrder> placeOrders);

	/**
	 * 撤单 Cancel Order
	 * 
	 * @param cancelOrder
	 * @return
	 */
	@POST("/api/v5/trade/cancel-order")
	Call<JSONObject> cancelOrder(@Body CancelOrder cancelOrder);

	/**
	 * 批量撤单 Cancel Multiple Orders
	 * 
	 * @param cancelOrders
	 * @return
	 */
	@POST("/api/v5/trade/cancel-batch-orders")
	Call<JSONObject> cancelMultipleOrders(@Body List<CancelOrder> cancelOrders);

	/**
	 * 修改订单 Amend Order
	 * 
	 * @param amendOrder
	 * @return
	 */
	@POST("/api/v5/trade/amend-order")
	Call<JSONObject> amendOrder(@Body AmendOrder amendOrder);

	/**
	 * 批量修改订单 Amend Multiple Orders
	 * 
	 * @param amendOrders
	 * @return
	 */
	@POST("/api/v5/trade/amend-batch-orders")
	Call<JSONObject> amendMultipleOrders(@Body List<AmendOrder> amendOrders);

	/**
	 * 市价仓位全平 Close Positions
	 * 
	 * @param closePositions
	 * @return
	 */
	@POST("/api/v5/trade/close-position")
	Call<JSONObject> closePositions(@Body ClosePositions closePositions);

	/**
	 * 获取订单信息 Get Order Details
	 * 
	 * @param instId
	 * @param ordId
	 * @param clOrdId
	 * @return
	 */
	@GET("/api/v5/trade/order")
	Call<JSONObject> getOrderDetails(@Query("instId") String instId, @Query("ordId") String ordId,
			@Query("clOrdId") String clOrdId);

	/**
	 * 获取未成交订单列表 Get Order List
	 * 
	 * @param instType
	 * @param uly
	 * @param instId
	 * @param ordType
	 * @param state
	 * @param after
	 * @param before
	 * @param limit
	 * @return
	 */
	@GET("/api/v5/trade/orders-pending")
	Call<JSONObject> getOrderList(@Query("instType") String instType, @Query("uly") String uly,
			@Query("instId") String instId, @Query("ordType") String ordType, @Query("state") String state,
			@Query("after") String after, @Query("before") String before, @Query("limit") String limit);

	/**
	 * 获取历史订单记录（近七天） Get Order History (last 7 days）
	 * 
	 * @param instType
	 * @param uly
	 * @param instId
	 * @param ordType
	 * @param state
	 * @param after
	 * @param before
	 * @param limit
	 * @return
	 */
	@GET("/api/v5/trade/orders-history")
	Call<JSONObject> getOrderHistory7days(@Query("instType") String instType, @Query("uly") String uly,
			@Query("instId") String instId, @Query("ordType") String ordType, @Query("state") String state,
			@Query("after") String after, @Query("before") String before, @Query("limit") String limit);

	/**
	 * 获取历史订单记录（近三个月） Get Order History (last 3 months)
	 * 
	 * @param instType
	 * @param uly
	 * @param instId
	 * @param ordType
	 * @param state
	 * @param after
	 * @param before
	 * @param limit
	 * @return
	 */
	@GET("/api/v5/trade/orders-history-archive")
	Call<JSONObject> getOrderHistory3months(@Query("instType") String instType, @Query("uly") String uly,
			@Query("instId") String instId, @Query("ordType") String ordType, @Query("state") String state,
			@Query("after") String after, @Query("before") String before, @Query("limit") String limit);

	/**
	 * 获取成交明细 Get Transaction Details
	 * 
	 * @param instType
	 * @param uly
	 * @param instId
	 * @param ordId
	 * @param after
	 * @param before
	 * @param limit
	 * @return
	 */
	@GET("/api/v5/trade/fills")
	Call<JSONObject> getTransactionDetails(@Query("instType") String instType, @Query("uly") String uly,
			@Query("instId") String instId, @Query("ordId") String ordId, @Query("after") String after,
			@Query("before") String before, @Query("limit") String limit);

	/**
	 * 委托策略下单 Place Algo Order
	 * 
	 * @param jsonObject
	 * @return
	 */
	@POST("/api/v5/trade/order-algo")
	Call<JSONObject> placeAlgoOrder(@Body JSONObject jsonObject);

	/**
	 * 撤销策略委托订单 Cancel Algo Order
	 * 
	 * @param cancelAlgoOrder
	 * @return
	 */
	@POST("/api/v5/trade/cancel-algos")
	Call<JSONObject> cancelAlgoOrders(@Body List<CancelAlgoOrder> cancelAlgoOrder);

	/**
	 * 获取未完成策略委托单列表 Get Algo Order List
	 * 
	 * @param algoId
	 * @param instType
	 * @param instId
	 * @param ordType
	 * @param after
	 * @param before
	 * @param limit
	 * @return
	 */
	@GET("/api/v5/trade/orders-algo-pending")
	Call<JSONObject> getAlgoOrderList(@Query("algoId") String algoId, @Query("instType") String instType,
			@Query("instId") String instId, @Query("ordType") String ordType, @Query("after") String after,
			@Query("before") String before, @Query("limit") String limit);

	/**
	 * 获取历史策略委托单列表 Get Algo Order History
	 * 
	 * @param state
	 * @param algoId
	 * @param instType
	 * @param instId
	 * @param ordType
	 * @param after
	 * @param before
	 * @param limit
	 * @return
	 */
	@GET("/api/v5/trade/orders-algo-history")
	Call<JSONObject> getAlgoOrderHistory(@Query("state") String state, @Query("algoId") String algoId,
			@Query("instType") String instType, @Query("instId") String instId, @Query("ordType") String ordType,
			@Query("after") String after, @Query("before") String before, @Query("limit") String limit);

}
