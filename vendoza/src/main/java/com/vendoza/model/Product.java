package com.vendoza.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private Long id;
    private String name;
    private String category;
    private double price;
    private double discountPrice;
    private String imageUrl;
    private String description;
    private boolean isOnSale;
    private int stock;
    private double rating;
    private int reviewCount;
    private List<Review> reviews = new ArrayList<>();
    private boolean samplesLoaded = false;

    // ========== CONSTRUCTOR UNTUK BACKEND (yang akan dipanggil CartService) ==========
    public Product(Long id, String name, Double price, String description, String imageUrl, String category, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price != null ? price : 0.0;
        this.description = description != null ? description : "";
        this.imageUrl = imageUrl != null ? imageUrl : "";
        this.category = category != null ? category : "";
        this.stock = stock != null ? stock : 0;
        this.discountPrice = 0;
        this.isOnSale = false;
        this.rating = 4.0;
        this.reviewCount = 0;
    }

    // Constructor lengkap (dengan field baru)
    public Product(Long id, String name, String category, double price, double discountPrice,
                   String imageUrl, String description, boolean isOnSale, int stock,
                   double rating, int reviewCount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.discountPrice = discountPrice;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isOnSale = isOnSale;
        this.stock = stock;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    // Constructor lama tetap ada agar tidak breaking change di DataService (dengan int)
    public Product(int id, String name, String category, double price, double discountPrice,
                   String imageUrl, String description, boolean isOnSale, int stock) {
        this((long) id, name, category, price, discountPrice, imageUrl, description, isOnSale, stock,
                4.0, 0);
    }

    // Constructor untuk DataService (int version)
    public Product(int id, String name, String category, double price, double currentPrice,
                   String description, String imageUrl, boolean isOnSale, double rating) {
        this((long) id, name, category, price, currentPrice, imageUrl, description, isOnSale, 100, rating, 0);
    }

    // ========== METHOD UNTUK SAMPLE REVIEWS ==========
    // ========== METHOD UNTUK SAMPLE REVIEWS (BERBEDA PER PRODUK) ==========
    public void loadSampleReviewsIfNeeded() {
        if (!samplesLoaded) {
            addSampleReviewsForProduct();
            samplesLoaded = true;
            // Update rating setelah sample reviews ditambahkan
            updateRatingFromReviews();
        }
    }

    private void addSampleReviewsForProduct() {
        String productName = name.toLowerCase();
        String productCategory = category.toLowerCase();

        // ========== REVIEW BERDASARKAN NAMA PRODUK ==========

        // Oversized Brown Blazer
        if (productName.contains("blazer")) {
            reviews.add(new Review("Fashionista99", "Perfect oversized fit! The brown color is so elegant. Material is high quality and comfortable.", 5, "3 days ago"));
            reviews.add(new Review("StyleGuru", "Love this blazer! Worth every penny. Looks expensive and feels premium.", 5, "1 week ago"));
            reviews.add(new Review("BusinessWoman", "Great for office wear. A bit pricey but the quality is outstanding.", 4, "2 weeks ago"));
            reviews.add(new Review("TrendySoul", "Received many compliments! The cut is very flattering.", 5, "5 days ago"));
        }

        // Cream Linen Shirt
        else if (productName.contains("linen") || (productName.contains("cream") && productName.contains("shirt"))) {
            reviews.add(new Review("CasualDude", "Super comfortable! The linen is breathable and perfect for hot weather.", 5, "5 days ago"));
            reviews.add(new Review("FashionLover", "Nice cream color, fits well. A little see-through but great with an undershirt.", 4, "1 week ago"));
            reviews.add(new Review("BeachVibes", "Perfect for vacation! Lightweight and stylish.", 5, "2 weeks ago"));
            reviews.add(new Review("Minimalist", "Love the relaxed fit. Goes with everything!", 5, "4 days ago"));
        }

        // Cargo Pants Khaki
        else if (productName.contains("cargo")) {
            reviews.add(new Review("StreetwearKing", "Best cargo pants I've ever bought! So many useful pockets.", 5, "2 days ago"));
            reviews.add(new Review("UrbanTeen", "Very trendy! Runs slightly large, size down for better fit.", 4, "1 week ago"));
            reviews.add(new Review("DailyDriver", "Comfortable for all-day wear. The khaki color is versatile.", 5, "6 days ago"));
            reviews.add(new Review("StyleHunter", "Great quality material. Zippers work smoothly.", 4, "2 weeks ago"));
        }

        // Leather Tote Bag
        else if (productName.contains("tote") || (productName.contains("leather") && productName.contains("bag"))) {
            reviews.add(new Review("LuxuryLover", "Stunning bag! The leather is premium quality and smells amazing.", 5, "4 days ago"));
            reviews.add(new Review("DailyCommuter", "Spacious and stylish. Fits my laptop perfectly. My go-to work bag now.", 5, "2 weeks ago"));
            reviews.add(new Review("ValueSeeker", "Bit expensive but worth every penny for the quality.", 4, "3 weeks ago"));
            reviews.add(new Review("BagCollector", "The caramel color is beautiful! Very well made.", 5, "1 week ago"));
        }

        // Wide Leg Pants
        else if (productName.contains("wide leg")) {
            reviews.add(new Review("ElegantLady", "Love the high waist! Very flattering silhouette.", 5, "1 day ago"));
            reviews.add(new Review("OfficeWorker", "Comfortable for all-day wear at the office. The belt is a nice touch.", 4, "5 days ago"));
            reviews.add(new Review("FashionBlogger", "Perfect flowy pants! Makes me look taller.", 5, "1 week ago"));
            reviews.add(new Review("PetiteGirl", "I'm 5'2\", length is perfect with heels.", 4, "2 weeks ago"));
        }

        // Vintage Denim Jacket
        else if (productName.contains("denim") || productName.contains("jacket")) {
            reviews.add(new Review("VintageSoul", "Classic piece! The wash is perfect. Fits like a dream.", 5, "6 days ago"));
            reviews.add(new Review("StyleSeeker", "Great vintage wash. A bit stiff at first but softens after a few wears.", 4, "2 weeks ago"));
            reviews.add(new Review("LayeredLook", "Perfect for layering. Goes with dresses and pants alike.", 5, "1 week ago"));
            reviews.add(new Review("DenimLover", "Quality denim! Buttons are sturdy.", 5, "3 days ago"));
        }

        // Silk Scarf
        else if (productName.contains("scarf") || productName.contains("silk")) {
            reviews.add(new Review("AccessoryQueen", "Beautiful silk! The pattern is gorgeous and vibrant.", 5, "3 days ago"));
            reviews.add(new Review("GiftGiver", "Bought as a gift, the recipient absolutely loved it!", 5, "1 week ago"));
            reviews.add(new Review("TravelStyle", "Perfect accessory to elevate any outfit. Lightweight and packable.", 5, "2 weeks ago"));
            reviews.add(new Review("LuxuryTouch", "Feels very expensive. Great quality silk.", 4, "5 days ago"));
        }

        // Cropped Knit Sweater
        else if (productName.contains("cropped") || productName.contains("knit")) {
            reviews.add(new Review("CozyGirl", "Super soft and warm! Perfect cropped length for high-waisted pants.", 5, "2 days ago"));
            reviews.add(new Review("WinterLover", "Great quality knit. Slightly tight around the arms but stretches.", 4, "1 week ago"));
            reviews.add(new Review("LayerQueen", "Perfect for layering over dresses. So versatile!", 5, "4 days ago"));
            reviews.add(new Review("TextureLover", "Love the ribbed texture! Very stylish.", 5, "1 week ago"));
        }

        // Leather Sneakers
        else if (productName.contains("sneakers") || (productName.contains("leather") && productName.contains("sneaker"))) {
            reviews.add(new Review("SneakerHead", "Very comfortable! Premium leather feels great right out of the box.", 5, "4 days ago"));
            reviews.add(new Review("DailyWalker", "Good support for daily wear. True to size.", 4, "2 weeks ago"));
            reviews.add(new Review("StyleHunter", "Looks expensive! Got many compliments on the first day.", 5, "3 weeks ago"));
            reviews.add(new Review("ComfortFirst", "No break-in period needed. Highly recommend!", 5, "1 week ago"));
        }

        // Straw Beach Hat
        else if (productName.contains("hat") || productName.contains("straw")) {
            reviews.add(new Review("VacationMode", "Perfect beach hat! Lightweight and provides good sun protection.", 5, "1 day ago"));
            reviews.add(new Review("SunProtector", "Great coverage. A bit floppy in strong wind but stylish.", 4, "5 days ago"));
            reviews.add(new Review("Poolside", "Looks so chic! Fits perfectly.", 5, "2 weeks ago"));
            reviews.add(new Review("TravelReady", "Packs flat in suitcase! Love this hat.", 4, "1 week ago"));
        }

        // Butterfly Necklace
        else if (productName.contains("butterfly")) {
            reviews.add(new Review("JewelryLover", "Absolutely stunning! The butterfly detail is so delicate and beautiful.", 5, "2 days ago"));
            reviews.add(new Review("GiftReceiver", "Got this as a gift from my sister, wearing it every day now!", 5, "1 week ago"));
            reviews.add(new Review("ElegantSoul", "Delicate and charming. Perfect for everyday wear or special occasions.", 5, "2 weeks ago"));
            reviews.add(new Review("DetailOriented", "Love the craftsmanship! Looks more expensive than it is.", 5, "3 days ago"));
        }

        // Bracelet
        else if (productName.contains("bracelet")) {
            reviews.add(new Review("Minimalist", "Simple yet elegant. Stackable with other bracelets for a layered look.", 5, "3 days ago"));
            reviews.add(new Review("GiftGiver", "Bought for my sister's birthday, she absolutely loves it!", 4, "1 week ago"));
            reviews.add(new Review("EverydayWear", "Great for daily wear. Doesn't tarnish.", 5, "2 weeks ago"));
            reviews.add(new Review("DelicatePiece", "Very dainty and pretty. Perfect for small wrists.", 5, "5 days ago"));
        }

        // ========== REVIEW BERDASARKAN KATEGORI (fallback) ==========

        // Kategori Women
        else if (productCategory.contains("women")) {
            reviews.add(new Review("StyleIcon", "Love this piece! Very feminine and elegant design.", 5, "3 days ago"));
            reviews.add(new Review("FashionForward", "Great addition to my wardrobe. Quality is impressive.", 4, "1 week ago"));
            reviews.add(new Review("DailyWear", "Comfortable and stylish. Would buy again!", 5, "2 weeks ago"));
        }

        // Kategori Men
        else if (productCategory.contains("men")) {
            reviews.add(new Review("MensStyle", "Great fit and quality. Highly recommended!", 5, "4 days ago"));
            reviews.add(new Review("CasualLook", "Perfect for daily wear. Very comfortable.", 4, "1 week ago"));
            reviews.add(new Review("ValueBuy", "Worth the price. Good quality material.", 4, "2 weeks ago"));
        }

        // Kategori Accessories / Jewelry
        else if (productCategory.contains("accessories") || productCategory.contains("jewelry")) {
            reviews.add(new Review("AccessoryAddict", "Beautiful piece! Adds perfect touch to any outfit.", 5, "3 days ago"));
            reviews.add(new Review("GiftGuide", "Perfect gift idea! Presentation box was nice.", 5, "1 week ago"));
            reviews.add(new Review("StyleUpgrade", "Elevates any simple outfit. Love it!", 4, "2 weeks ago"));
        }

        // Kategori Footwear
        else if (productCategory.contains("footwear")) {
            reviews.add(new Review("ShoeLover", "Very comfortable! True to size. Would buy again.", 5, "5 days ago"));
            reviews.add(new Review("DailyDriver", "Great for all-day wear. No blisters!", 4, "1 week ago"));
            reviews.add(new Review("WorthIt", "Quality materials. Looks great with everything.", 5, "2 weeks ago"));
        }

        // Default review untuk produk lain
        else {
            reviews.add(new Review("HappyCustomer", "Great product! Fast shipping and good quality.", 5, "1 week ago"));
            reviews.add(new Review("VerifiedBuyer", "Exactly as described. Would recommend!", 4, "2 weeks ago"));
            reviews.add(new Review("SatisfiedUser", "Very happy with my purchase. Will buy again.", 5, "3 weeks ago"));
        }

        // Tambahkan 1 review default untuk semua produk (agar konsisten)
        reviews.add(new Review("ShopLover", "Love shopping here! Product quality is consistent.", 5, "1 week ago"));
    }

    private void updateRatingFromReviews() {
        if (!reviews.isEmpty()) {
            double total = 0;
            for (Review r : reviews) {
                total += r.getStars();
            }
            this.rating = total / reviews.size();
            this.reviewCount = reviews.size();
        }
    }

    // ========== GETTERS ==========
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public double getDiscountPrice() { return discountPrice; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public boolean isOnSale() { return isOnSale; }
    public int getStock() { return stock; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }

    public List<Review> getReviews() {
        loadSampleReviewsIfNeeded();
        return reviews;
    }

    // Method untuk mendapatkan semua reviews (termasuk sample)
    public List<Review> getAllReviews() {
        loadSampleReviewsIfNeeded();
        return new ArrayList<>(reviews);
    }

    // Total review count (termasuk sample)
    public int getTotalReviewCount() {
        loadSampleReviewsIfNeeded();
        return reviews.size();
    }

    // ========== SETTERS (untuk update dari backend) ==========
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setDiscountPrice(double discountPrice) { this.discountPrice = discountPrice; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setOnSale(boolean onSale) { isOnSale = onSale; }
    public void setStock(int stock) { this.stock = stock; }
    public void setRating(double rating) { this.rating = rating; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    // ========== HELPER METHODS ==========
    public double getCurrentPrice() {
        return discountPrice > 0 ? discountPrice : price;
    }

    public double getDiscountPercent() {
        if (discountPrice > 0 && price > 0) {
            return Math.round(((price - discountPrice) / price) * 100);
        }
        return 0;
    }

    public String getStockLabel() {
        if (stock <= 0)  return "Habis";
        if (stock <= 10) return "Stok Terbatas (" + stock + ")";
        return "Tersedia (" + stock + ")";
    }

    public String getStockColor() {
        if (stock <= 0)  return "#E53935";
        if (stock <= 10) return "#F57C00";
        return "#388E3C";
    }

    public String getStarString() {
        double avg = getAverageRating();
        int full  = (int) avg;
        int half  = (avg - full >= 0.5) ? 1 : 0;
        int empty = 5 - full - half;
        return "★".repeat(full) + (half == 1 ? "⯨" : "") + "☆".repeat(empty);
    }

    public boolean isAvailable() {
        return stock > 0;
    }

    public void addReview(Review review) {
        loadSampleReviewsIfNeeded();
        reviews.add(0, review);
        updateRatingFromReviews();
    }

    // Rating dihitung dari semua review (termasuk sample)
    public double getAverageRating() {
        loadSampleReviewsIfNeeded();

        if (reviews.isEmpty()) {
            return rating; // fallback ke rating awal
        }

        double total = 0;
        for (Review r : reviews) {
            total += r.getStars();
        }
        return total / reviews.size();
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', category='" + category +
                "', price=" + price + ", rating=" + rating + ", stock=" + stock + "}";
    }
}
