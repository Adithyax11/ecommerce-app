import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";

function Products() {
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [categoryId, setCategoryId] = useState("");
    const [sort, setSort] = useState("");

    useEffect(() => {
        api.get("/categories").then((res) => setCategories(res.data));
    }, []);

    useEffect(() => {
        const params = {};
        if (categoryId) params.categoryId = categoryId;
        if (sort) params.sort = sort;
        api.get("/products", { params }).then((res) => setProducts(res.data.content));
    }, [categoryId, sort]);

    return (
        <div className="p-6">
            <h2 className="text-2xl font-bold mb-4">Menu</h2>
            <div className="flex gap-4 mb-6">
                <select value={categoryId} onChange={(e) => setCategoryId(e.target.value)} className="border p-2 rounded">
                    <option value="">All Stalls</option>
                    {categories.map((c) => (
                        <option key={c.id} value={c.id}>{c.name}</option>
                    ))}
                </select>
                <select value={sort} onChange={(e) => setSort(e.target.value)} className="border p-2 rounded">
                    <option value="">Sort</option>
                    <option value="price,asc">Price: Low to High</option>
                    <option value="price,desc">Price: High to Low</option>
                </select>
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                {products.map((p) => (
                    <Link key={p.id} to={`/products/${p.id}`} className="border rounded-lg p-4 block hover:shadow-md">
                        <h3 className="font-semibold">{p.name}</h3>
                        <p className="text-gray-500 text-sm">{p.description}</p>
                        <p className="text-orange-600 font-bold mt-2">₹{p.price}</p>
                    </Link>
                ))}
            </div>
        </div>
    );
}

export default Products;