import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Brand } from 'src/app/entity/Brand.model';
import { Category } from 'src/app/entity/Category.model';
import { Product } from 'src/app/entity/Product.model';
import { BrandService } from 'src/app/services/brand.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent implements OnInit {
  categoryId: String;
  listOfCategory: Category[] = [];
  listOfBrand: Brand[] = [];
  price = [0, 30000000];
  listOfData: Product[] = [];
  pageSize = 6;
  pageIndex = 1;
  totalProduct: Number;
  status:String = 'default';
  controlArray: Map<string, any> = new Map<string, any>();
  totalPrice:number = 0;
  cart: Product[] = [];

  constructor(
    route: ActivatedRoute,
    private activatedRoute: ActivatedRoute,
    private categoryService: CategoryService,
    private brandService: BrandService,
    private productService: ProductService,
    private notification: NzNotificationService
  ) {
    route.params.subscribe((val) => {
      this.categoryId = this.activatedRoute.snapshot.params.category || '';
    });
  }

  ngOnInit(): void {
    // declare cart 
    const cart = localStorage.getItem('cart') || '';
    if (cart) {
      this.cart = JSON.parse(cart);
    }
    this.getCategories();
    this.getBrands();
    if (this.categoryId || !(this.categoryId === '')) {
      this.controlArray.set('categoryID', this.categoryId);
    }
    this.getProducts(this.pageIndex, this.pageSize, 'productID', 'descend');
  }

  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }
  getProducts(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.controlArray.set('sortField', sortField);
    this.controlArray.set('sortOrder', sortOrder);
    // get product
    this.productService.getProducts(this.controlArray).subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfData = data.results;
          this.totalProduct = data.rowCount;
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }
  getProductSort(param: any) {
    // get product
    this.productService.getProducts(this.controlArray).subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfData = data.results;
          this.totalProduct = data.rowCount;
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }
  getCategories() {
    this.categoryService.getAllCategory().subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfCategory = data.results;
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }
  getBrands() {
    this.brandService.getAllBrand().subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfBrand = data.results;
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }
  // load by price
  priceChange(value: number[] | number): void {
    const priceFrom = value[0] || 0;
    const priceTo = value[1] || 0;
    this.controlArray.set('pageIndex', 1);
    this.controlArray.set('pageSize', 6);
    this.controlArray.set('priceFrom', priceFrom);
    this.controlArray.set('priceTo', priceTo);
    this.getProductSort(this.controlArray);
  }
  //load product
  onPageIndexChange(value: number): void {
    this.controlArray.set('pageIndex', value);
    this.getProductSort(this.controlArray);
  }
  onPageSizeChange(value: number): void {
    this.controlArray.set('pageSize', value);
    this.getProductSort(this.controlArray);
  }
  filterByBrand(id) {
    if (id) {
      this.controlArray.set('pageIndex', 1);
      this.controlArray.set('pageSize', 6);
      this.controlArray.set('brandID', id);
      this.getProductSort(this.controlArray);
    }
  }
  changeStatus(e) {
    if(e){
      if(e=="p-des"){
        this.getProducts(this.pageIndex, this.pageSize, 'productPrice', 'descend');
      }
      if(e=="p-asc"){
        this.getProducts(this.pageIndex, this.pageSize, 'productPrice', 'ascend');
      }
      if(e=="default"){
        this.getProducts(this.pageIndex, this.pageSize, 'productID', 'descend');
      }
    }
  }

  addToCart(product:Product){
    let duplicate = false;
    this.cart.forEach((ele) => {
      if (ele.productID == product.productID ) {
        duplicate = true;
      }
    });
    // if no => add item
    if(!duplicate){
      product.quanlityBuy = 1;
      this.cart.push(product);
      this.updateCart();
      this.createNotification(
        'success',
        'Sản phẩm đã thêm vào giỏ hàng',
        ''
      );
    }else{
      this.createNotification(
        'info',
        'Sản phẩm đã có trong giỏ hàng',
        ''
      );
    }
  }
  updateCart() {
    this.totalPrice = 0;
    this.cart.forEach((ele) => {
      this.totalPrice += ele.quanlityBuy * ele.productPrice;
    });
    localStorage.setItem('total', this.totalPrice.toString());
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }
}
