<x-layouts.app title="Laporan Keuangan">
    <!-- Page Header -->
    <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
            <h1 class="text-2xl font-bold text-gray-900">Laporan Keuangan</h1>
            <p class="mt-1 text-sm text-gray-500">Analisis pendapatan, pengeluaran, dan profit bisnis Anda.</p>
        </div>
        <div class="flex items-center gap-3">
            <x-button variant="outline">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                </svg>
                Export PDF
            </x-button>
            <x-button variant="outline">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                </svg>
                Export Excel
            </x-button>
        </div>
    </div>

    <!-- Date Range Picker -->
    <x-card class="mb-6">
        <div class="flex flex-col sm:flex-row sm:items-center gap-4">
            <div class="flex items-center gap-2">
                <span class="text-sm text-gray-600">Periode:</span>
                <x-input type="date" name="date_from" />
                <span class="text-gray-400">-</span>
                <x-input type="date" name="date_to" />
            </div>
            <div class="flex items-center gap-2">
                <button type="button" class="px-3 py-1.5 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">Hari Ini</button>
                <button type="button" class="px-3 py-1.5 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">Minggu Ini</button>
                <button type="button" class="px-3 py-1.5 text-sm bg-emerald-100 text-emerald-700 rounded-lg font-medium">Bulan Ini</button>
                <button type="button" class="px-3 py-1.5 text-sm text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">Tahun Ini</button>
            </div>
            <x-button variant="primary" class="sm:ml-auto">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
                </svg>
                Refresh
            </x-button>
        </div>
    </x-card>

    <!-- Summary Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <!-- Revenue -->
        <x-card class="bg-gradient-to-br from-emerald-500 to-emerald-600 border-0">
            <div class="text-white">
                <div class="flex items-center justify-between mb-4">
                    <p class="text-emerald-100 font-medium">Total Pendapatan</p>
                    <div class="p-2 bg-white/20 rounded-lg">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                    </div>
                </div>
                <p class="text-3xl font-bold font-mono">Rp 0</p>
                <div class="mt-2 flex items-center gap-2 text-sm">
                    <span>Belum ada data periode ini</span>
                </div>
            </div>
        </x-card>

        <!-- Expense -->
        <x-card class="bg-gradient-to-br from-red-500 to-red-600 border-0">
            <div class="text-white">
                <div class="flex items-center justify-between mb-4">
                    <p class="text-red-100 font-medium">Total Pengeluaran</p>
                    <div class="p-2 bg-white/20 rounded-lg">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"></path>
                        </svg>
                    </div>
                </div>
                <p class="text-3xl font-bold font-mono">Rp 0</p>
                <div class="mt-2 flex items-center gap-2 text-sm">
                    <span>Belum ada data periode ini</span>
                </div>
            </div>
        </x-card>

        <!-- Profit -->
        <x-card class="bg-gradient-to-br from-blue-500 to-blue-600 border-0">
            <div class="text-white">
                <div class="flex items-center justify-between mb-4">
                    <p class="text-blue-100 font-medium">Profit Bersih</p>
                    <div class="p-2 bg-white/20 rounded-lg">
                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                        </svg>
                    </div>
                </div>
                <p class="text-3xl font-bold font-mono">Rp 0</p>
                <div class="mt-2 flex items-center gap-2 text-sm">
                    <span>Belum ada data periode ini</span>
                </div>
            </div>
        </x-card>
    </div>

    <!-- Charts Row -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        <!-- Revenue vs Expense Chart -->
        <x-card>
            <x-slot name="header">
                <div class="flex items-center justify-between">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-900">Pendapatan vs Pengeluaran</h3>
                        <p class="text-sm text-gray-500">Perbandingan bulanan</p>
                    </div>
                    <select class="text-sm border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500">
                        <option>Tahun 2026</option>
                        <option>Tahun 2025</option>
                    </select>
                </div>
            </x-slot>
            
            <!-- Chart Placeholder -->
            <div class="h-72 flex items-center justify-center bg-gradient-to-br from-gray-50 to-gray-100 rounded-lg">
                <div class="text-center">
                    <svg class="w-16 h-16 mx-auto text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                    </svg>
                    <p class="mt-3 text-sm text-gray-500 font-medium">Belum ada data</p>
                    <p class="text-xs text-gray-400">Grafik akan muncul setelah ada transaksi</p>
                </div>
            </div>
        </x-card>

        <!-- Profit Trend Chart -->
        <x-card>
            <x-slot name="header">
                <div class="flex items-center justify-between">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-900">Tren Profit</h3>
                        <p class="text-sm text-gray-500">Perkembangan profit harian</p>
                    </div>
                    <select class="text-sm border-gray-300 rounded-lg focus:ring-emerald-500 focus:border-emerald-500">
                        <option>30 Hari</option>
                        <option>90 Hari</option>
                    </select>
                </div>
            </x-slot>
            
            <!-- Chart Placeholder -->
            <div class="h-72 flex items-center justify-center bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg">
                <div class="text-center">
                    <svg class="w-16 h-16 mx-auto text-blue-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z"></path>
                    </svg>
                    <p class="mt-3 text-sm text-blue-600 font-medium">Belum ada data</p>
                    <p class="text-xs text-blue-400">Grafik akan muncul setelah ada transaksi</p>
                </div>
            </div>
        </x-card>
    </div>

    <!-- Breakdown Tables -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
        <!-- Revenue Breakdown -->
        <x-card :padding="false">
            <x-slot name="header">
                <h3 class="text-lg font-semibold text-gray-900">Breakdown Pendapatan</h3>
            </x-slot>
            
            <x-table>
                <x-slot name="header">
                    <x-table-cell type="header">Kategori</x-table-cell>
                    <x-table-cell type="header" align="right">Total</x-table-cell>
                    <x-table-cell type="header" align="right">%</x-table-cell>
                </x-slot>
                
                <!-- Empty State -->
                <tr>
                    <td colspan="3" class="px-6 py-8 text-center">
                        <p class="text-gray-500">Belum ada data pendapatan</p>
                    </td>
                </tr>
            </x-table>
        </x-card>

        <!-- Expense Breakdown -->
        <x-card :padding="false">
            <x-slot name="header">
                <h3 class="text-lg font-semibold text-gray-900">Breakdown Pengeluaran</h3>
            </x-slot>
            
            <x-table>
                <x-slot name="header">
                    <x-table-cell type="header">Kategori</x-table-cell>
                    <x-table-cell type="header" align="right">Total</x-table-cell>
                    <x-table-cell type="header" align="right">%</x-table-cell>
                </x-slot>
                
                <!-- Empty State -->
                <tr>
                    <td colspan="3" class="px-6 py-8 text-center">
                        <p class="text-gray-500">Belum ada data pengeluaran</p>
                    </td>
                </tr>
            </x-table>
        </x-card>
    </div>

    <!-- Daily Report Table -->
    <x-card :padding="false">
        <x-slot name="header">
            <div class="flex items-center justify-between">
                <h3 class="text-lg font-semibold text-gray-900">Laporan Harian</h3>
                <x-button variant="outline" size="sm">
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"></path>
                    </svg>
                    Download
                </x-button>
            </div>
        </x-slot>
        
        <x-table>
            <x-slot name="header">
                <x-table-cell type="header">Tanggal</x-table-cell>
                <x-table-cell type="header" align="right">Transaksi</x-table-cell>
                <x-table-cell type="header" align="right">Pendapatan</x-table-cell>
                <x-table-cell type="header" align="right">Pengeluaran</x-table-cell>
                <x-table-cell type="header" align="right">Profit</x-table-cell>
            </x-slot>
            
            <!-- Empty State -->
            <tr>
                <td colspan="5" class="px-6 py-12 text-center">
                    <div class="flex flex-col items-center justify-center">
                        <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mb-4">
                            <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                            </svg>
                        </div>
                        <p class="text-gray-600 font-medium">Belum ada laporan</p>
                        <p class="text-sm text-gray-400 mt-1">Laporan akan muncul setelah ada transaksi</p>
                    </div>
                </td>
            </tr>
        </x-table>
    </x-card>
</x-layouts.app>
