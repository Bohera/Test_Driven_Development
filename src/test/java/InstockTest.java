import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class InstockTest {

    private Instock instock;

    @Before
    public void setUp(){
        this.instock = new Instock();
    }

    @Test
    public void test_Count_Should_Be_Zero_When_Instock_Empty() {
        assertEquals(0, instock.getCount());
    }

    @Test
    public void test_Count_Should_Be_Five_When_Instock_Has_Five_Products() {
        int productsCount = 5;
        addProductsToInstock(productsCount);
        assertEquals(productsCount, instock.getCount());
    }


    @Test
    public void test_Contains_Should_Be_True_or_False_When_Product_Present_or_Not() {
        Product product = addProductsToInstock(5)[3];
        assertTrue(instock.contains(product));
        assertFalse(instock.contains(new Product("not_present", 1, 1)));
    }

    @Test
    public void test_FindReturns_The_Correct_Product() {
        Product expected = addProductsToInstock(10)[4];
        Product actual = instock.find(4);
        assertNotNull(actual);
        assertEquals(expected.getLabel(), actual.getLabel());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_Find_Should_Throw_With_Negative_Index() {
        instock.find(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_Find_Should_Throw_With_Index_Greater_ThanLast() {
        addProductsToInstock(2);
        instock.find(instock.getCount());
    }

    @Test
    public void test_Change_Quantity_Should_Update_The_Product() {
        Product product = new Product("test", 1, 10);
        instock.add(product);
        int expected = product.getQuantity() + 5;
        instock.changeQuantity(product.getLabel(), expected);
        assertEquals(expected, product.getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_Change_Quantity_Throws_When_No_Product_With_That_Label() {
        instock.changeQuantity("john dow", 10);
    }

    @Test
    public void test_Find_By_Label_Should_Return_The_Correct_Product() {
        Product expected = addProductsToInstock(10)[3];
        Product actual = instock.findByLabel(expected.getLabel());
        assertNotNull(actual);
        assertEquals(expected.getLabel(), actual.getLabel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_Find_By_Label_Throws_When_No_Product_With_That_Label() {
        instock.findByLabel("john dow");
    }

    @Test
    public void test_FindFirstMostExpensiveProducts_Should_Return_TheCorrectProduct() {
        int productsToTake = 10;

        List<Product> expected = Arrays.stream(addProductsToInstock(20))
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(productsToTake)
                .collect(Collectors.toList());

        Iterable<Product> iterable = instock.findFirstMostExpensiveProducts(productsToTake);

        List<Product> actual = asserIterableNotNullAndConvertToList(iterable);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_FindFirstMostExpensiveProducts_Should_Throw_When_There_Are_LessProducts() {
        addProductsToInstock(10);

        instock.findFirstMostExpensiveProducts(11);
    }

    private List<Product> asserIterableNotNullAndConvertToList(Iterable<Product> iterable) {
        assertNotNull(iterable);

        List<Product> products = new ArrayList<>();

        iterable.forEach(products::add);

        return products;
    }

    private Product[] addProductsToInstock(int count) {

        Product[] arr = new Product[count];

        for (int i = 1; i <= count; i++) {
            Product product = new Product("product_" + i, 10.00 + i, i);
            instock.add(product);
            arr[i - 1] = product;
        }
        return arr;
    }
}