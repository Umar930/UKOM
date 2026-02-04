<x-layouts.app title="Dashboard">
    <!-- Page Header -->
    <div class="mb-8">
        <h1 class="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p class="mt-1 text-sm text-gray-500">Selamat datang kembali! Berikut ringkasan bisnis Anda hari ini.</p>
    </div>

    <!-- KPI Cards -->
    <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4 mb-8">
        <!-- Total Revenue -->
        <x-stat-card 
            title="Total Pendapatan"
            value="Rp 0"
            change="0%"
            changeType="neutral"
        >
            <x-slot name="icon">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
            </x-slot>
        </x-stat-card>

        <!-- Total Transactions -->
        <x-stat-card 
            title="Total Transaksi"
            value="0"
            change="0%"
            changeType="neutral"
            iconBg="bg-blue-100"
            iconColor="text-blue-600"
        >
            <x-slot name="icon">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path>
                </svg>
            </x-slot>
        </x-stat-card>

        <!-- Total Products -->
        <x-stat-card 
            title="Total Produk"
            value="0"
            change="0"
            changeType="neutral"
            iconBg="bg-purple-100"
            iconColor="text-purple-600"
        >
            <x-slot name="icon">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
                </svg>
            </x-slot>
        </x-stat-card>

        <!-- Low Stock Alerts -->
        <x-stat-card 
            title="Stok Rendah"
            value="0"
            change="-"
            changeType="neutral"
            iconBg="bg-amber-100"
            iconColor="text-amber-600"
        >
            <x-slot name="icon">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
                </svg>
            </x-slot>
        </x-stat-card>
    </div>

    <!-- Charts Row -->
    <div class="grid grid-cols-1 gap-6 lg:grid-cols-2 mb-8">
        <!-- Sales Trend Chart -->
        <x-card>
            <x-slot name="header">
                <div class="flex items-center justify-between">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-900">Tren Penjualan</h3>
                        <p class="text-sm text-gray-500">7 hari terakhir</p>
                    </div>
                    <select class="text-sm border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500">
                        <option>7 Hari</option>
                        <option>30 Hari</option>
                        <option>90 Hari</option>
                    </select>
                </div>
            </x-slot>
            
            <!-- Chart Placeholder -->
            <div class="h-64 flex items-center justify-center bg-gradient-to-br from-emerald-50 to-emerald-100 rounded-lg">
                <div class="text-center">
                    <svg class="w-12 h-12 mx-auto text-emerald-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z"></path>
                    </svg>
                    <p class="mt-2 text-sm text-emerald-600 font-medium">Belum ada data penjualan</p>
                    <p class="text-xs text-emerald-500">Data akan muncul setelah ada transaksi</p>
                </div>
            </div>
            
            <!-- Quick Stats -->
            <div class="mt-4 grid grid-cols-3 gap-4">
                <div class="text-center p-3 bg-gray-50 rounded-lg">
                    <p class="text-2xl font-bold text-gray-900 font-mono">Rp 0</p>
                    <p class="text-xs text-gray-500">Hari Ini</p>
                </div>
                <div class="text-center p-3 bg-gray-50 rounded-lg">
                    <p class="text-2xl font-bold text-gray-900 font-mono">Rp 0</p>
                    <p class="text-xs text-gray-500">Minggu Ini</p>
                </div>
                <div class="text-center p-3 bg-gray-50 rounded-lg">
                    <p class="text-2xl font-bold text-gray-900 font-mono">Rp 0</p>
                    <p class="text-xs text-gray-500">Bulan Ini</p>
                </div>
            </div>
        </x-card>

        <!-- Best Selling Products Chart -->
        <x-card>
            <x-slot name="header">
                <div class="flex items-center justify-between">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-900">Produk Terlaris</h3>
                        <p class="text-sm text-gray-500">Bulan ini</p>
                    </div>
                    <a href="{{ route('products') }}" class="text-sm text-emerald-600 hover:text-emerald-700 font-medium">
                        Lihat Semua
                    </a>
                </div>
            </x-slot>
            
            <!-- Empty State -->
            <div class="py-12 flex flex-col items-center justify-center text-center">
                <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mb-4">
                    <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
                    </svg>
                </div>
                <p class="text-gray-500 font-medium">Belum ada produk terlaris</p>
                <p class="text-sm text-gray-400 mt-1">Tambahkan produk dan transaksi untuk melihat data</p>
            </div>
        </x-card>
    </div>

    <!-- Recent Transactions -->
    <x-card>
        <x-slot name="header">
            <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
                <div>
                    <h3 class="text-lg font-semibold text-gray-900">Transaksi Terbaru</h3>
                    <p class="text-sm text-gray-500">Daftar transaksi hari ini</p>
                </div>
                <div class="flex items-center gap-3">
                    <x-search-input placeholder="Cari transaksi..." class="w-64" />
                    <a href="{{ route('transactions') }}" class="btn btn-primary">
                        Lihat Semua
                    </a>
                </div>
            </div>
        </x-slot>
        
        <!-- Empty State -->
        <div class="py-12 flex flex-col items-center justify-center text-center">
            <div class="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mb-4">
                <svg class="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
                </svg>
            </div>
            <p class="text-gray-600 font-medium text-lg">Belum ada transaksi</p>
            <p class="text-sm text-gray-400 mt-1 max-w-sm">Transaksi akan muncul di sini setelah Anda melakukan penjualan</p>
        </div>
    </x-card>
</x-layouts.app>
