package domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    //代表购物车上的个数不确定的购物项,键:商品pid,值:购物项
    private Map<String, CartItem> map=new HashMap<String, CartItem>();

    private double total=0;

    //1_向购物车中添加购物项
    public void addCart(CartItem item){
        //获取到待添加到购物车中商品id
        String pid=item.getProduct().getPid();
        if(map.containsKey(pid)){
            CartItem old=map.get(pid);
            old.setNum(old.getNum()+item.getNum());
        }else{
            map.put(pid, item);
        }

        //total=total+item.getSubTotal();
    }

    //2_从购物车中移除单个购物项
    public void delCart(String pid){
        map.remove(pid);

        //total=total-map.get(pid).getSubTotal();
    }
    //3_清空购物车
    public void clearCart(){
        map.clear();

        //total=0;
    }

    //计算总计
    public double getTotal() {
        total=0;
        for(CartItem item:map.values()){
            total=total+item.getSubTotal();
        }
        return total;
    }

    //为了方便遍历MAP中的所有的购物项,提供以下方法
    public  Collection<CartItem> getCartItems(){
        return map.values();
    }


    public Map<String, CartItem> getMap() {
        return map;
    }

    public void setMap(Map<String, CartItem> map) {
        this.map = map;
    }



    public void setTotal(double total) {
        this.total = total;
    }


}
