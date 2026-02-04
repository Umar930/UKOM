<x-layouts.app title="Produk & Stok">
    <!-- Page Header -->
    <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
            <h1 class="text-2xl font-bold text-gray-900">Produk & Stok</h1>
            <p class="mt-1 text-sm text-gray-500">Kelola semua produk dan inventaris toko Anda.</p>
        </div>
        <div class="flex items-center gap-3">
            <x-button variant="outline" @click="$dispatch('open-modal-import-product')">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path>
                </svg>
                Import
            </x-button>
            <x-button @click="$dispatch('open-modal-add-product')">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                </svg>
                Tambah Produk
            </x-button>
        </div>
    </div>

    <!-- Filters -->
    <x-card class="mb-6">
        <div class="flex flex-col lg:flex-row lg:items-center gap-4">
            <div class="flex-1">
                <x-search-input placeholder="Cari produk berdasarkan nama, SKU, atau barcode..." />
            </div>
            <div class="flex flex-wrap items-center gap-3">
                <x-select 
                    name="category" 
                    :options="[
                        'all' => 'Semua Kategori',
                        'makanan' => 'Makanan',
                        'minuman' => 'Minuman',
                        'snack' => 'Snack',
                        'rokok' => 'Rokok',
                        'toiletries' => 'Toiletries',
                        'lainnya' => 'Lainnya'
                    ]"
                    selected="all"
                    placeholder=""
                />
                <x-select 
                    name="status" 
                    :options="[
                        'all' => 'Semua Status',
                        'active' => 'Aktif',
                        'inactive' => 'Non-aktif',
                        'low_stock' => 'Stok Rendah',
                        'out_of_stock' => 'Habis'
                    ]"
                    selected="all"
                    placeholder=""
                />
                <x-button variant="secondary">
                    <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z"></path>
                    </svg>
                    Filter
                </x-button>
            </div>
        </div>
    </x-card>

    <!-- Products Table -->
    <x-card :padding="false">
        <x-table>
            <x-slot name="header">
                <x-table-cell type="header" width="50px">
                    <input type="checkbox" class="h-4 w-4 text-emerald-600 border-gray-300 rounded focus:ring-emerald-500">
                </x-table-cell>
                <x-table-cell type="header">Produk</x-table-cell>
                <x-table-cell type="header">Kategori</x-table-cell>
                <x-table-cell type="header" align="center">Stok</x-table-cell>
                <x-table-cell type="header" align="right">Harga Beli</x-table-cell>
                <x-table-cell type="header" align="right">Harga Jual</x-table-cell>
                <x-table-cell type="header" align="center">Status</x-table-cell>
                <x-table-cell type="header" align="center">Aksi</x-table-cell>
            </x-slot>

            <!-- Empty State -->
            <tr>
                <td colspan="8" class="px-6 py-16 text-center">
                    <div class="flex flex-col items-center justify-center">
                        <div class="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mb-4">
                            <svg class="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
                            </svg>
                        </div>
                        <p class="text-gray-600 font-medium text-lg">Belum ada produk</p>
                        <p class="text-sm text-gray-400 mt-1 max-w-sm">Klik tombol "Tambah Produk" untuk menambahkan produk pertama Anda</p>
                        <x-button class="mt-4" @click="$dispatch('open-modal-add-product')">
                            <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                            </svg>
                            Tambah Produk Pertama
                        </x-button>
                    </div>
                </td>
            </tr>
        </x-table>
    </x-card>

    <!-- Add Product Modal -->
    <x-modal id="add-product" title="Tambah Produk Baru" size="lg">
        <form class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="md:col-span-2">
                    <x-input name="name" label="Nama Produk" placeholder="Masukkan nama produk" required />
                </div>
                
                <x-input name="sku" label="SKU / Barcode" placeholder="SKU-001" required />
                
                <x-select 
                    name="category" 
                    label="Kategori"
                    :options="[
                        'makanan' => 'Makanan',
                        'minuman' => 'Minuman',
                        'snack' => 'Snack',
                        'rokok' => 'Rokok',
                        'toiletries' => 'Toiletries',
                        'lainnya' => 'Lainnya'
                    ]"
                    required
                />
                
                <x-input type="number" name="buy_price" label="Harga Beli" placeholder="0" prefix="Rp" required />
                
                <x-input type="number" name="sell_price" label="Harga Jual" placeholder="0" prefix="Rp" required />
                
                <x-input type="number" name="stock" label="Stok Awal" placeholder="0" required />
                
                <x-input type="number" name="min_stock" label="Stok Minimum" placeholder="10" hint="Notifikasi akan muncul jika stok di bawah nilai ini" />
            </div>
            
            <x-textarea name="description" label="Deskripsi (Opsional)" placeholder="Deskripsi singkat produk..." rows="3" />
        </form>
        
        <x-slot name="footer">
            <x-button variant="secondary" @click="$dispatch('close-modal-add-product')">Batal</x-button>
            <x-button type="submit">Simpan Produk</x-button>
        </x-slot>
    </x-modal>

    <!-- Edit Product Modal -->
    <x-modal id="edit-product" title="Edit Produk" size="lg">
        <form class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div class="md:col-span-2">
                    <x-input name="name" label="Nama Produk" placeholder="Masukkan nama produk" required />
                </div>
                
                <x-input name="sku" label="SKU / Barcode" placeholder="SKU-001" required />
                
                <x-select 
                    name="category" 
                    label="Kategori"
                    :options="[
                        'makanan' => 'Makanan',
                        'minuman' => 'Minuman',
                        'snack' => 'Snack',
                        'rokok' => 'Rokok',
                        'toiletries' => 'Toiletries',
                        'lainnya' => 'Lainnya'
                    ]"
                    required
                />
                
                <x-input type="number" name="buy_price" label="Harga Beli" placeholder="0" prefix="Rp" required />
                
                <x-input type="number" name="sell_price" label="Harga Jual" placeholder="0" prefix="Rp" required />
                
                <x-input type="number" name="stock" label="Stok" placeholder="0" required />
                
                <x-input type="number" name="min_stock" label="Stok Minimum" placeholder="10" />
            </div>
            
            <x-textarea name="description" label="Deskripsi (Opsional)" placeholder="Deskripsi singkat produk..." rows="3" />
        </form>
        
        <x-slot name="footer">
            <x-button variant="secondary" @click="$dispatch('close-modal-edit-product')">Batal</x-button>
            <x-button type="submit">Update Produk</x-button>
        </x-slot>
    </x-modal>
</x-layouts.app>
